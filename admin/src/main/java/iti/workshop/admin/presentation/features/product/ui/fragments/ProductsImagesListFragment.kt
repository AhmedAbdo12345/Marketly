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
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentListImagesBinding
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductImagesOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.dialogs.AddImageDialog
import iti.workshop.admin.presentation.features.product.ui.dialogs.AddVariantDialog
import iti.workshop.admin.presentation.features.product.ui.dialogs.ImagePreviewDialog
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.alertDialog
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsImagesListFragment : Fragment() {

    var actionType:Action = Action.Add
    var product:Product? = null
    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentListImagesBinding
    lateinit var adapter: ProductImagesAdapter

    private fun updateUISate() {
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
            viewModel.productImageListResponses.collect{ state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStates(DataStates.Error)
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
                        dataViewStates(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStates(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStates(DataStates.Data)
                        adapter.submitList(state.data)

                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.product_fragment_list_images,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ProductImagesAdapter(ProductImagesOnCLickListener(::selectImage,::deleteImage))

        binding.mAdapter = adapter
        updateUISate()
        addImageNewOne()
        updateImage()
        return binding.root
    }

    private fun updateImage() {
        val bundle = arguments
        if (bundle != null) {
            actionType = bundle.getSerializable(ConstantsKeys.ACTION_KEY) as Action
            product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
            if (actionType == Action.Edit){
                viewModel.retrieveImagesProductFromServer(product_id = product?.id?:-1)
            }
        }
    }
    private fun addImageNewOne() {
        binding.addAction.setOnClickListener {
            val dialogFragment = AddImageDialog(viewModel, product?.id?:-1)
            dialogFragment.show(requireActivity().supportFragmentManager, "AddImageDialog")
        }
    }

    private fun deleteImage(model: Image) {
        requireContext().alertDialog("Delete Action","Do you want delete ${model.alt} ? \n Are you sure?",{
            viewModel.deleteImageProductFromServer(model.product_id?:-1,model.id?:-1)
        },{

        })
    }

    private fun selectImage(model: Image) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.IMAGE_KEY,model)
        findNavController().navigate(R.id.action_productsImagesListFragment_to_imagePreviewDialog,bundle)
     }



    private fun dataViewStates(dataStates:DataStates) {
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
}

