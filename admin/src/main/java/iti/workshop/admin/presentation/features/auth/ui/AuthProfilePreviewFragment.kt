package iti.workshop.admin.presentation.features.auth.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.Constants
import iti.workshop.admin.data.remote.firestore.FireStoreManager
import iti.workshop.admin.data.local.shared.SharedManager
import iti.workshop.admin.databinding.AuthFragmentProfilePreviewBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.features.auth.viewModel.AuthViewModel
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.hideSoftKey
import iti.workshop.admin.presentation.utils.loadingDialog
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class AuthProfilePreviewFragment : Fragment(),View.OnClickListener {


    private val TAKE_PICTURE = 6352
    private val REQUEST_CAMERA_ACCESS_PERMISSION = 5674
    private var bitmap: Bitmap? = null
    var storageRef: FirebaseStorage? = null
    private var user:User? = null
    private lateinit var sharedManager: SharedManager
    private val viewModel: AuthViewModel by viewModels()


    lateinit var binding: AuthFragmentProfilePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_profile_preview, container, false)
        sharedManager = SharedManager.getInstance(requireContext())
        storageRef = FirebaseStorage.getInstance(Constants.STORAGE_PATH)

        setEventClickListner()

        user = sharedManager.getUser()
        binding.model = user
        updateUser()
        return binding.root
    }

    private fun setEventClickListner() {
        binding.upload.setOnClickListener(this)
        binding.editImage.setOnClickListener(this)
        binding.username.setOnClickListener {
            binding.username.visibility = View.GONE
            binding.productTitleInput.setText(binding.username.text.toString())
            binding.productTitleHolder.visibility = View.VISIBLE
        }

        binding.productTitleInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val username =  binding.username.text.toString()
                if (username.isBlank()){
                    binding.username.error = "You should to put username"
                    false
                }else{
                    binding.productTitleHolder.visibility = View.GONE
                    binding.username.visibility = View.VISIBLE
                    binding.username.text = binding.productTitleInput.text.toString()
                    requireActivity().hideSoftKey()
                    uploadUsernameToServer()
                    true
                }
            } else {
                false
            }
        }
    }


    private fun updateUser() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(ConstantsKeys.USER_KEY)) {
            user = bundle.getSerializable(ConstantsKeys.USER_KEY) as User
            binding.model = user
        }
    }

    private fun persistImage(bitmap: Bitmap?, name: String): File {
        val filesDir = requireActivity().applicationContext.filesDir
        val imageFile = File(filesDir, "$name.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
        return imageFile
    }

    private fun uploadToServer() {

        if (bitmap!=null){
            uploadImageToServer()

        }else{
            uploadUsernameToServer()
        }



    }

    private fun uploadUsernameToServer() {
        val dialog: ProgressDialog = requireContext().loadingDialog()
        dialog.show()
        user?.let {
            it.name = binding.username.text.toString()
            FireStoreManager.saveUser(it) {
                sharedManager.saveUser(user)
                dialog.dismiss()
                Message.snakeMessage(
                    requireContext(),
                    binding.root,
                    "user data uploaded successfully",
                    true
                )?.show()
            }
        }
    }

    private fun uploadImageToServer() {
        val dialog: ProgressDialog = requireContext().loadingDialog()
        dialog.show()

        val imageFile: File = persistImage(bitmap, "profile")
        val folderName = "profiles"
        val profileImage = storageRef!!.reference.child(folderName + "/" + user?.name + ".jpg")
        val file = Uri.fromFile(imageFile)
        val uploadTask = profileImage.putFile(file)

        val urlTask = uploadTask.continueWithTask<Uri> { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }

            // Continue with the task to get the download URL
            profileImage.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                user?.let {
                    it.name = binding.username.text.toString()
                    it.image = downloadUri.toString()
                    FireStoreManager.saveUser(it) {
                        dialog.dismiss()

                        Message.snakeMessage(
                            requireContext(),
                            binding.root,
                            "user data uploaded successfully",
                            true
                        )?.show()
                        sharedManager.saveUser(user)
                    }
                }

            }
        }.addOnFailureListener { e ->
            dialog.dismiss()
            Message.snakeMessage(
                requireContext(),
                binding.root,
                e.message,
                false
            )?.show()
        }
    }

    private fun getImageFromCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePicture, TAKE_PICTURE)
        }
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {REQUEST_CAMERA_ACCESS_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromCamera()
            }

            else -> {}
        }
    }

    override fun onClick(viewBtn: View?) {
        when (viewBtn!!.id) {
//            R.id.fromCamera -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && ActivityCompat.checkSelfPermission(viewBtn.context, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(arrayOf(Manifest.permission.CAMERA),REQUEST_CAMERA_ACCESS_PERMISSION)
//            } else {
//                getImageFromCamera()
//            }

            R.id.edit_image -> chooseImage(requireContext())
            R.id.upload ->  uploadToServer()
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery"
        )

        val builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(optionsMenu) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    bitmap = data.extras!!["data"] as Bitmap?

                    binding.fromGallery.setImageBitmap(bitmap)
                    uploadImageToServer()
                }

                1 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor =
                            requireActivity().contentResolver.query(selectedImage, filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)
                            bitmap = BitmapFactory.decodeFile(picturePath)
                            binding.fromGallery.setImageBitmap(bitmap)
                            uploadImageToServer()

                            cursor.close()
                        }
                    }
                }
            }
        }
    }

}