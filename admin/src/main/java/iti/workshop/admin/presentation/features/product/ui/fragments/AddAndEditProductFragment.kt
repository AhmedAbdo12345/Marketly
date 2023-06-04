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
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentEditAndAddBinding
import iti.workshop.admin.databinding.ProductFragmentListProductsBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.product.ui.adapters.ItemOnCLickListener
import iti.workshop.admin.presentation.features.product.ui.adapters.ProductsAdapter
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataResponseState
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddAndEditProductFragment : Fragment() {

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
        return binding.root
    }

    private fun updateUIStates() {
        lifecycleScope.launch {
            viewModel.productResponses.collect { state ->
                when (state) {
                    is DataResponseState.OnError -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataResponseState.OnLoading -> {
                    }
                    is DataResponseState.OnNothingData -> {
                    }
                    is DataResponseState.OnSuccess -> {
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
            product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
            binding.dataModel = product
        }
    }


}