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
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductFragmentListVariantsBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsAdapter
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductVariantsOnCLickListener
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.alert
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsVariantsListFragment : Fragment() {

    var productId:Long = -1
    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentListVariantsBinding
    lateinit var adapter: ProductVariantsAdapter

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
            viewModel.productVariantsListResponses.collect{ state->
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

        binding = DataBindingUtil.inflate(inflater,R.layout.product_fragment_list_products,container,false)

        binding.lifecycleOwner = this
        adapter = ProductVariantsAdapter(ProductVariantsOnCLickListener(::selectVariant,::deleteVariant))

        binding.mAdapter = adapter
        // calls
        updateUISate();addImageNewOne();updateVatiant()
        return binding.root
    }

    private fun updateVatiant() {
        val bundle = arguments
        if (bundle != null) {
            productId = bundle.getLong(ConstantsKeys.PRODUCT_KEY)
            viewModel.retrieveVariantsProductFromServer(productId)
        }
    }
    private fun addImageNewOne() {
        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add New One", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteVariant(model: Variant) {
        requireContext().alert("Delete Action","Do you want delete ${model.title} ? \n Are you sure?",{
            viewModel.deleteVariantProductFromServer(model.product_id?:-1,model.id?:-1)
        },{

        })
    }

    private fun selectVariant(model: Variant) {
        Toast.makeText(requireContext(), "Select Variant", Toast.LENGTH_SHORT).show()
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

