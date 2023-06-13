package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.PriceRule
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentEditAndAddBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.comon.ProductAction
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddAndEditProductFragment : Fragment() {

    var actionType:ProductAction = ProductAction.Add
    var product: Product = Product()

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
        binding.lifecycleOwner = this

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
        val model:Product =  when(actionType){
            ProductAction.Add -> {
                Product(
                    title = binding.titleProductInput.text.toString(),
                    body_html = "<p>${binding.descriptionInput.text.toString()}</p>"
                )
            }
            ProductAction.Edit -> {
                    product.copy(
                        title = binding.titleProductInput.text.toString(),
                        body_html = "<p>${binding.descriptionInput.text.toString()}</p>"
                    )
            }
        }
        viewModel.addOrEditProduct(actionType,model)
    }

    private fun isValidData(): Boolean {
        if (binding.titleProductInput.text.isNullOrBlank()) {
            binding.titleProductInput.error = "Please put name of product"
            return false
        }
        if (binding.descriptionInput.text.isNullOrBlank()) {
            binding.titleProductInput.error = "Please put some description"
            return false
        }
        return true
    }

    private fun productActionNavigate() {
        if (actionType == ProductAction.Edit) {
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
        binding.addProductImage.setOnClickListener {
            Toast.makeText(requireContext(), "Image Upload", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUIStates() {
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
            actionType = bundle.getSerializable(ConstantsKeys.PRODUCT_ACTION_KEY) as ProductAction
            if (actionType == ProductAction.Edit){
                binding.actionGroup.visibility = View.VISIBLE
                product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
                binding.dataModel = product
            }

        }
    }


}