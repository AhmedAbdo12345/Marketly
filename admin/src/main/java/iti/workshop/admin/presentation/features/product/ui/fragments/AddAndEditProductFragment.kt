package iti.workshop.admin.presentation.features.product.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.Constants
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductFragmentEditAndAddBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.chooseImage
import iti.workshop.admin.presentation.utils.loadingDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class AddAndEditProductFragment : Fragment() {

    var actionType:Action = Action.Add
    var product: Product = Product()
    private var bitmap: Bitmap? = null
    lateinit var loadingDialog:ProgressDialog
    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding: ProductFragmentEditAndAddBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.product_fragment_edit_and_add,
            container,
            false
        )
        loadingDialog =  requireContext().loadingDialog()
        binding.lifecycleOwner = viewLifecycleOwner


        updateProduct()
        updateUIStates()
        uploadProductImage()
        productActionNavigate()
        saveProduct()
        return binding.root
    }

    private fun saveProduct() {
        binding.saveActionBtn.setOnClickListener {
            if (isValidData()) { saveData() }
        }
    }

    private fun saveData() {
        if (::loadingDialog.isInitialized){
            loadingDialog.show()
        }
        val model:Product =  when(actionType){
            Action.Add -> {
                Product(
                    title = binding.productTitleInput.text.toString(),
                    body_html = "<p>${binding.descriptionInput.text.toString()}</p>",
                    variants = listOf(Variant(price = binding.valueInput.text.toString())),
                    image = generateImage(),
                    images = listOf(
                        generateImage()
                    )
                )
            }
            Action.Edit -> {
                    product.copy(
                        title = binding.productTitleInput.text.toString(),
                        body_html = "<p>${binding.descriptionInput.text.toString()}</p>",
                        image = generateImage(),
                        images = listOf(
                            generateImage()
                        )
                    )
            }
        }
        viewModel.addOrEditProduct(actionType,model)
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
                alt = binding.productTitleInput.text.toString(),
                attachment = base64,
                filename = binding.productTitleInput.text.toString()+type
            )
        }
        return null
    }

    private fun isValidData(): Boolean {
        if (binding.productTitleInput.text.isNullOrBlank()) {
            binding.productTitleInput.error = "Please put name of product"
            return false
        }
        if (binding.descriptionInput.text.isNullOrBlank()) {
            binding.descriptionInput.error = "Please put some description"
            return false
        }

        if (binding.valueInput.text.isNullOrBlank()) {
            binding.valueInput.error = "Please put value of product"
            return false
        }


        return true
    }

    private fun productActionNavigate() {
        if (actionType == Action.Edit) {
            val bundle = Bundle()
            bundle.putLong(ConstantsKeys.PRODUCT_KEY, product.id)

            binding.addProductImages.setOnClickListener {
                findNavController().navigate(R.id.action_addAndEditProductFragment_to_productsImagesListFragment,bundle)
            }
            binding.addProductVariants.setOnClickListener {
                findNavController().navigate(R.id.action_addAndEditProductFragment_to_productsVariantsListFragment,bundle)

            }
        }
    }

    private fun uploadProductImage() {
        binding.productImage.setOnLongClickListener {
            requireActivity().chooseImage()
            true

        }
        binding.addProductImage.setOnClickListener {

            requireActivity().chooseImage()

//            if(checkAndRequestPermissions(requireActivity())){
//            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    bitmap = data.extras!!["data"] as Bitmap?
                    binding.productImage.setImageBitmap(bitmap)
                    binding.addProductImage.visibility = View.GONE
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
                            binding.productImage.setImageBitmap(bitmap)
                            binding.addProductImage.visibility = View.GONE

                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateUIStates() {
        lifecycleScope.launch {
            viewModel.actionResponse.collect{ state ->
                if (loadingDialog.isShowing){
                    loadingDialog.dismiss()
                }
                state.first?.let {
                    Message.snakeMessage(requireContext(), binding.root, state.second, it)?.show()
                    if (it){
                        binding.productTitleInput.setText("")
                        binding.descriptionInput.setText("")
                        binding.valueInput.setText("")
                        binding.productImage.setImageDrawable(resources.getDrawable(R.drawable.bags))
                        delay(500)
                        findNavController().popBackStack()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.productListResponses.collect { state ->
                when (state) {
                    is DataListResponseState.OnError -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataListResponseState.OnLoading -> {
                    }
                    is DataListResponseState.OnNothingData -> {
                    }
                    is DataListResponseState.OnSuccess -> {
                        Toast.makeText(requireContext(), "Data Has been Added", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            }
        }
    }

    private fun updateProduct() {
        val bundle = arguments
        if (bundle != null) {
            actionType = bundle.getSerializable(ConstantsKeys.ACTION_KEY) as Action
            if (actionType == Action.Edit){
                binding.addProductImage.visibility = View.GONE
                binding.actionGroup.visibility = View.VISIBLE
                product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
                binding.dataModel = product
            }

        }
    }



}