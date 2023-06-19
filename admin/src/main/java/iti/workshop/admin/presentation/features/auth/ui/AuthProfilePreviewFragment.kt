package iti.workshop.admin.presentation.features.auth.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.Constants
import iti.workshop.admin.data.remote.firestore.FireStoreManager
import iti.workshop.admin.data.shared.SharedManager
import iti.workshop.admin.databinding.AuthFragmentProfilePreviewBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.features.auth.viewModel.AuthViewModel
import iti.workshop.admin.presentation.utils.loadingDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class AuthProfilePreviewFragment : Fragment(),View.OnClickListener {

    private val PICK_IMAGE = 12345
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
        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            binding.fromCamera.visibility = View.GONE
        }
        user = sharedManager.getUser()
        binding.model = user
        updateUser()
        navigateToEdit()
        return binding.root
    }

    private fun setEventClickListner() {
        binding.upload.setOnClickListener(this)
        binding.fromGallery.setOnClickListener(this)
        binding.fromCamera.setOnClickListener(this)
    }


    private fun updateUser() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(ConstantsKeys.USER_KEY)) {
            user = bundle.getSerializable(ConstantsKeys.USER_KEY) as User
            binding.model = user
        }
    }
    private fun navigateToEdit() {

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

    private fun uploadImageToServer() {
        val dialog: ProgressDialog = requireContext().loadingDialog()
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
                    it.image = downloadUri.toString()
                    FireStoreManager.saveUser(it) {
                        dialog.dismiss()
                        Toast.makeText(context, "data Uploaded", Toast.LENGTH_SHORT).show()
                        sharedManager.saveUser(user)
                    }
                }

            }
        }.addOnFailureListener { e ->
            dialog.dismiss()
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageFromCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePicture, TAKE_PICTURE)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    val inputStream = requireActivity().contentResolver.openInputStream(
//                        data?.data!!
//                    )
//                    bitmap = BitmapFactory.decodeStream(inputStream)
//                    binding.fromGallery.setImageBitmap(bitmap)
//                } catch (e: FileNotFoundException) {
//                    e.printStackTrace()
//                }
//            }
//        } else if (requestCode == TAKE_PICTURE) {
//            if (resultCode == Activity.RESULT_OK) {
//                val extras = data?.extras
//                bitmap = extras!!["data"] as Bitmap?
//                binding.fromGallery.setImageBitmap(bitmap)
//            }
//        }
//    }

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

            R.id.fromGallery -> chooseImage(requireContext())
            R.id.upload -> if (bitmap != null) uploadImageToServer()
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
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
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
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

                            cursor.close()
                        }
                    }
                }
            }
        }
    }

}