package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentListProductsBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.features.product.ui.adapters.ItemOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductsAdapter
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.alert
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentListProductsBinding
    lateinit var adapter: ProductsAdapter

    private fun searchByProductTitle(){
        binding.searchView.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(searchQuery: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.queryProductByTitle(searchQuery)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
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
            viewModel.productListResponses.collect{ state->
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
                        adapter.submitList(state.data.products)

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

        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ProductsAdapter(ItemOnCLickListener(::selectProduct,::deleteProduct))

        binding.mAdapter = adapter
        viewModel.getCountOfProducts()
        updateUISate()
        searchByProductTitle()
        navigateToAddNewProduct()

        return binding.root
    }

    private fun navigateToAddNewProduct() {
        binding.floatingActionButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ConstantsKeys.ACTION_KEY,Action.Add)
            findNavController().navigate(R.id.action_productsListFragment_to_addAndEditProductFragment,bundle)
        }
    }

    private fun deleteProduct(product: Product) {
        requireContext().alert("Delete Action","Do you want delete ${product.title} ? \n Are you sure?",{
            viewModel.deleteProduct(product)
        },{

        })
    }

    private fun selectProduct(product: Product) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.PRODUCT_KEY, product)
        bundle.putSerializable(ConstantsKeys.ACTION_KEY, Action.Edit)
        findNavController().navigate(R.id.action_productsListFragment_to_previewProductFragment,bundle)
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

