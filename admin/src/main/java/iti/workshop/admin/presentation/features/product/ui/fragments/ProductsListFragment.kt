package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentListProductsBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.product.ui.adapters.ItemOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductsAdapter
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataResponseState
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentListProductsBinding
    lateinit var adapter: ProductsAdapter

    private fun updateUISate() {

        lifecycleScope.launch {
            viewModel.deleteResponse.collect { state ->
                if (state)
                    Toast.makeText(requireContext(), "Data has been deleted", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(requireContext(), "Error Happend", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        lifecycleScope.launch {
            viewModel.productResponses.collect{state->
                when(state){
                    is DataResponseState.OnError -> {
                        hideProgressBar()
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                state.message,
                                false
                            )?.show()
                        }
                    }
                    is DataResponseState.OnLoading -> {
                        showProgressBar()
                    }
                    is DataResponseState.OnNothingData -> {

                    }
                    is DataResponseState.OnSuccess -> {
                        hideProgressBar()
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

        binding.lifecycleOwner = this
        adapter = ProductsAdapter(ItemOnCLickListener(::selectProduct,::deleteProduct))

        binding.mAdapter = adapter
        viewModel.getCountOfProducts()
        updateUISate()
        navigateToAddNewProduct()

        return binding.root
    }

    private fun navigateToAddNewProduct() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_productsListFragment_to_addAndEditProductFragment)
        }
    }

    private fun deleteProduct(product: Product) {
        viewModel.deleteProduct(product)
    }

    private fun selectProduct(product: Product) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.PRODUCT_KEY, product)
        findNavController().navigate(R.id.action_productsListFragment_to_previewProductFragment,bundle)
    }

    private fun showProgressBar() {
        binding.shimmerResults.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.shimmerResults.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }


}