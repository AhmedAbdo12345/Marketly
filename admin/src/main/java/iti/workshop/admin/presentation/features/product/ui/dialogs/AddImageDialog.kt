package iti.workshop.admin.presentation.features.product.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.CouponDialogAddDiscountCodeBinding
import iti.workshop.admin.databinding.ProductDialogAddImageBinding
import iti.workshop.admin.databinding.ProductDialogAddVariantBinding
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.chooseImage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddImageDialog(
    private val viewModel:ProductViewModel,
    private val productId:Long = -1L
) : DialogFragment() {
    private var bitmap: Bitmap? = null
    lateinit var binding: ProductDialogAddImageBinding
    lateinit var loadingDialog: ProgressDialog

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.product_dialog_add_image,
            null,
            false
        )

        binding.addAction.setOnClickListener {
            if (isValidData()){
                saveData()
            }
        }
//        dialog?.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))

        lifecycleScope.launch {
            viewModel.actionResponse.collect{ state ->
                state.first?.let {
                    if (it) {
                        dismiss()
                    }
                }
            }
        }

        handleEvents()
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        return dialog
    }

    private fun handleEvents() {
        binding.addImageBtn.setOnClickListener {
            chooseImage(requireContext())
        }
        binding.imageProduct.setOnLongClickListener {
            chooseImage(requireContext())
            true
        }
    }


    private fun saveData() {
        if (productId == -1L) {
            Toast.makeText(requireContext(), "There is an unexpected error please try again", Toast.LENGTH_SHORT).show()
            return
        }
        val imagePost = generateImage()
        imagePost?.let {
            viewModel.addImage(productId,it)
        }
    }

    private fun isValidData(): Boolean {
       if( binding.titleInput.text.isNullOrBlank()){
           binding.titleInput.error = "Please put title of image"
           return  false
       }
        return true
    }


    private fun generateImage(): Image? {
        if (bitmap!=null){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            val type = when {
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 0, null) -> ".jpg"
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 0, null) -> ".pmg"
                bitmap!!.compress(Bitmap.CompressFormat.WEBP, 0, null) -> ".webp"
                else -> ".jpg"
            }
            return Image(
                attachment = base64,
                alt = binding.titleInput.text.toString(),
                filename = binding.titleInput.text.toString()+type,
            )
        }
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    bitmap = data.extras!!["data"] as Bitmap?
                    binding.imageProduct.setImageBitmap(bitmap)
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
                            binding.imageProduct.setImageBitmap(bitmap)

                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
        )

        val builder = AlertDialog.Builder(context)
        builder.setItems(optionsMenu) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {

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

}