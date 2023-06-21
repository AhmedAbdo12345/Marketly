package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductFragmentPreviewProductBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsOnCLickListener
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PreviewProductFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentPreviewProductBinding
    lateinit var imageAdapter: ProductImagesAdapter
    lateinit var variantAdapter: ProductVariantsAdapter
    lateinit var product:Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.product_fragment_preview_product,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        imageAdapter = ProductImagesAdapter(ProductImagesOnCLickListener(::selectImage,::deleteImage),true)
        binding.imagesAdapter = imageAdapter

        variantAdapter = ProductVariantsAdapter(ProductVariantsOnCLickListener(::selectVariant,::deleteVariant),true)
        binding.variantAdapter = variantAdapter

        updateProduct()
        updateUIState()
        navigateToEditProduct()
        return binding.root
    }

    private fun deleteVariant(variant: Variant) {
        TODO("Not yet implemented")
    }

    private fun selectVariant(variant: Variant) {

    }

    private fun deleteImage(image: Image) {
        TODO("Not yet implemented")
    }

    private fun selectImage(image: Image) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.IMAGE_KEY,image)
        findNavController().navigate(R.id.action_previewProductFragment_to_imagePreviewDialog,bundle)
    }

    private fun updateProduct() {
        val bundle = arguments
        if (bundle != null) {
            product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
            binding.dataModel = product
            viewModel.retrieveImagesProductFromServer(product_id = product.id)
            viewModel.retrieveVariantsProductFromServer(product_id = product.id)
        }
    }

    private fun updateUIState() {
        lifecycleScope.launch {
            viewModel.actionResponse.collect { state ->
                state.first?.let {
                    Message.snakeMessage(
                        requireContext(),
                        binding.root,
                        state.second,
                        it
                    )?.show()
                }

            }
        }

        lifecycleScope.launch {
            viewModel.productVariantsListResponses.collect{ state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStatesVariants(DataStates.Error)
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                message,
                                false
                            )?.show()
                        }
                    }
                    is DataListResponseState.OnLoading -> {
                        dataViewStatesVariants(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStatesVariants(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStatesVariants(DataStates.Data)
                        variantAdapter.submitList(state.data)

                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.productImageListResponses.collect{ state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStatesImages(DataStates.Error)
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                message,
                                false
                            )?.show()
                        }
                    }
                    is DataListResponseState.OnLoading -> {
                        dataViewStatesImages(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStatesImages(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStatesImages(DataStates.Data)
                        imageAdapter.submitList(state.data)

                    }
                }
            }
        }
    }

    private fun navigateToEditProduct() {
        binding.saveActionBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ConstantsKeys.PRODUCT_KEY, product)
            bundle.putSerializable(ConstantsKeys.ACTION_KEY, Action.Edit)
            findNavController().navigate(R.id.action_previewProductFragment_to_addAndEditProductFragment,bundle)
        }
    }


    private fun dataViewStatesVariants(dataStates: DataStates) {
        when(dataStates){
            DataStates.Data -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Nothing -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.VISIBLE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Error -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.VISIBLE
            }
            DataStates.Loading ->{
                binding.shimmerResults.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
        }

    }

    private fun dataViewStatesImages(dataStates: DataStates) {
        when(dataStates){
            DataStates.Data -> {
                binding.shimmerResultsImage.visibility = View.GONE
                binding.recyclerViewImage.visibility = View.VISIBLE
                binding.nothingDataResponseImage.visibility = View.GONE
                binding.errorDataResponseImage.visibility = View.GONE
            }
            DataStates.Nothing -> {
                binding.shimmerResultsImage.visibility = View.GONE
                binding.recyclerViewImage.visibility = View.GONE
                binding.nothingDataResponseImage.visibility = View.VISIBLE
                binding.errorDataResponseImage.visibility = View.GONE
            }
            DataStates.Error -> {
                binding.shimmerResultsImage.visibility = View.GONE
                binding.recyclerViewImage.visibility = View.GONE
                binding.nothingDataResponseImage.visibility = View.GONE
                binding.errorDataResponseImage.visibility = View.VISIBLE
            }
            DataStates.Loading ->{
                binding.shimmerResultsImage.visibility = View.VISIBLE
                binding.recyclerViewImage.visibility = View.GONE
                binding.nothingDataResponseImage.visibility = View.GONE
                binding.errorDataResponseImage.visibility = View.GONE
            }
        }

    }

}