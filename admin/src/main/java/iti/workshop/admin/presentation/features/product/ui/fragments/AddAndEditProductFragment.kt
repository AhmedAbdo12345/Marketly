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
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.dialogs.AddImageToLocalDialog
import iti.workshop.admin.presentation.features.product.ui.dialogs.AddVariantToLocalDialog
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.loadingDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class AddAndEditProductFragment : Fragment() {

    var actionType: Action = Action.Add
    var product: Product = Product()


    private var bitmap: Bitmap? = null
    lateinit var loadingDialog: ProgressDialog
    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding: ProductFragmentEditAndAddBinding


    // Variant and Images List Issues
    // Adapters
    lateinit var imageAdapter: ProductImagesAdapter
    lateinit var variantAdapter: ProductVariantsAdapter

    // Dialogs
    lateinit var addImageDialog: AddImageToLocalDialog
    lateinit var addVariantDialog: AddVariantToLocalDialog

    // MutableLists

    var variantsList: MutableList<Variant> = mutableListOf()
    var imagesList: MutableList<Image?> = mutableListOf()
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
        loadingDialog = requireContext().loadingDialog()
        binding.lifecycleOwner = viewLifecycleOwner

        adapterHandler()
        dialogHandler()
        checkIfListOfVariantsIsNull()
        checkIfListOfImagesIsNull()


        updateProduct()
        updateUIStates()
        uploadProductImage()
        productActionNavigate()
        saveProduct()
        return binding.root
    }


    private fun saveProduct() {
        binding.saveActionBtn.setOnClickListener {
            if (isValidData()) {
                saveData()
            }
        }
    }

    private fun saveData() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.show()
        }



        val imageHeader = generateImage()

        val model: Product = when (actionType) {
            Action.Add -> {

                val varaintHeader =
                    Variant(title = "Main Product", price = binding.valueInput.text.toString())
                imagesList.add(0, imageHeader)
                variantsList.add(0, varaintHeader)

                Product(
                    title = binding.productTitleInput.text.toString(),
                    body_html = "<p>${binding.descriptionInput.text.toString()}</p>",
                    variants = variantsList,
                    image = imageHeader,
                    images = imagesList,
                    tags = binding.categoryTypeInput.text.toString(),
                    product_type = binding.productTypeInput.text.toString(),
                    vendor = binding.productVendorInput.text.toString()
                )
            }

            Action.Edit -> {
                product.copy(
                    title = binding.productTitleInput.text.toString(),
                    body_html = "<p>${binding.descriptionInput.text.toString()}</p>",
                    image = imageHeader,
                    tags = binding.categoryTypeInput.text.toString(),
                    product_type = binding.productTypeInput.text.toString(),
                    vendor = binding.productVendorInput.text.toString()
                )
            }
        }
        viewModel.addOrEditProduct(actionType, model)
    }

    private fun generateImage(): Image? {
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
//            val type = when {
//                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 0, null) -> ".jpg"
//                bitmap!!.compress(Bitmap.CompressFormat.PNG, 0, null) -> ".pmg"
//                bitmap!!.compress(Bitmap.CompressFormat.WEBP, 0, null) -> ".webp"
//                else -> ".jpg"
//            }
            return Image(
                alt = binding.productTitleInput.text.toString(),
                attachment = base64,
                filename = binding.productTitleInput.text.toString()
            )
        }
        return null
    }

    private fun isValidData(): Boolean {
        if (binding.productTitleInput.text.isNullOrBlank()) {
            binding.productTitleInput.error = "Please put name of product"
            return false
        }
        if (binding.valueInput.text.isNullOrBlank()) {
            binding.valueInput.error = "Please put value of product"
            return false
        }

        if (binding.valueInput.text.toString().toFloat() == 0f) {
            binding.valueInput.error = "Please put value of product more than Zero"
            return false
        }

        if (binding.valueInput.text.toString().toFloat() > 10000f) {
            binding.valueInput.error = "Please don't put value of product more than 10,000"
            return false
        }

        if (binding.productVendorInput.text.isNullOrBlank()) {
            binding.productVendorInput.error = "Please put vendor Name of product"
            return false
        }

        if (binding.categoryTypeInput.text.isNullOrBlank()) {
            binding.categoryTypeInput.error = "Please put Category type of product"
            return false
        }

        if (binding.productTypeInput.text.isNullOrBlank()) {
            binding.productTypeInput.error = "Please put Product type of product"
            return false
        }

        if (binding.descriptionInput.text.isNullOrBlank()) {
            binding.descriptionInput.error = "Please put some description"
            return false
        }

        return true
    }

    private fun productActionNavigate() {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.PRODUCT_KEY, product)
        bundle.putSerializable(ConstantsKeys.ACTION_KEY, actionType)

        binding.addProductImages.setOnClickListener {
            findNavController().navigate(
                R.id.action_addAndEditProductFragment_to_productsImagesListFragment,
                bundle
            )
        }
        binding.addProductVariants.setOnClickListener {
            findNavController().navigate(
                R.id.action_addAndEditProductFragment_to_productsVariantsListFragment,
                bundle
            )

        }

    }

    private fun uploadProductImage() {
        binding.productImage.setOnLongClickListener {
            chooseImage(requireActivity())
            true
        }
        binding.addProductImage.setOnClickListener {
            chooseImage(requireActivity())
//            if(checkAndRequestPermissions(requireActivity())){
//            }
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
                            requireActivity().contentResolver.query(
                                selectedImage,
                                filePathColumn,
                                null,
                                null,
                                null
                            )
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
            viewModel.actionResponse.collect { state ->
                if (loadingDialog.isShowing) {
                    loadingDialog.dismiss()
                }
                state.first?.let {
                    Message.snakeMessage(requireContext(), binding.root, state.second, it)?.show()
                    if (it) {
                        binding.productTitleInput.clearComposingText()
                        binding.descriptionInput.clearComposingText()
                        binding.valueInput.clearComposingText()
                        binding.productVendorInput.clearComposingText()
                        binding.productTypeInput.clearListSelection()
                        binding.categoryTypeInput.clearListSelection()
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
            if (actionType == Action.Edit) {
                binding.addProductImage.visibility = View.GONE
                binding.imageRecyclerHolder.visibility = View.GONE
                binding.variantRecyclerHolder.visibility = View.GONE
                product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
                binding.dataModel = product
//                variantsList= product.variants as MutableList<Variant>
//                imagesList = product.images as MutableList<Image?>

            } else {
                binding.editVariantsAndImagesHolder.visibility = View.GONE
            }

        }
    }


    // region Variant and Image Rank
    private fun AddImage(image: Image) {
        imagesList.add(image)
        imageAdapter.submitList(imagesList)
        imageAdapter.notifyItemInserted(imagesList.lastIndex)
        checkIfListOfImagesIsNull()
    }

    private fun AddVaraint(variant: Variant) {
        variantsList.add(variant)
        variantAdapter.submitList(variantsList)
        variantAdapter.notifyItemInserted(variantsList.lastIndex)
        checkIfListOfVariantsIsNull()
    }

    private fun deleteVariant(variant: Variant) {
        variantsList.remove(variant)
        variantAdapter.notifyItemRemoved(variantsList.indexOf(variant))
        checkIfListOfVariantsIsNull()
    }

    private fun selectVariant(variant: Variant) {

    }

    private fun deleteImage(image: Image) {
        imagesList.remove(image)
        variantAdapter.notifyItemRemoved(imagesList.indexOf(image))
        checkIfListOfImagesIsNull()
    }

    private fun selectImage(image: Image) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.IMAGE_KEY, image)
        findNavController().navigate(
            R.id.action_addAndEditProductFragment_to_imagePreviewDialog,
            bundle
        )
    }

    private fun dialogHandler() {
        addVariantDialog = AddVariantToLocalDialog(::AddVaraint)
        addImageDialog = AddImageToLocalDialog(::AddImage)
        binding.addPictureAction.setOnClickListener {
            addImageDialog.show(requireActivity().supportFragmentManager, "addPictureAction")
        }
        binding.addVariantAction.setOnClickListener {
            addVariantDialog.show(requireActivity().supportFragmentManager, "addVariantAction")
        }
    }


    private fun adapterHandler() {
        imageAdapter = ProductImagesAdapter(
            ProductImagesOnCLickListener(::selectImage, ::deleteImage), disableDelete = false,
            isBase64 = true
        )
        binding.imagesAdapter = imageAdapter

        variantAdapter = ProductVariantsAdapter(
            ProductVariantsOnCLickListener(::selectVariant, ::deleteVariant),
            disableDelete = false
        )
        binding.variantAdapter = variantAdapter

    }

    private fun checkIfListOfVariantsIsNull() {
        if (variantsList.isEmpty()) {
            binding.nothingDataResponse.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.nothingDataResponse.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

    }

    private fun checkIfListOfImagesIsNull() {
        if (imagesList.isEmpty()) {
            binding.nothingDataResponseImage.visibility = View.VISIBLE
            binding.recyclerViewImage.visibility = View.GONE
        } else {
            binding.nothingDataResponseImage.visibility = View.GONE
            binding.recyclerViewImage.visibility = View.VISIBLE
        }
    }

    // endregion Variant and Image Rank

}