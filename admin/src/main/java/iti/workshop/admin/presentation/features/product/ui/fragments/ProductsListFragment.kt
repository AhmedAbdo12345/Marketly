package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import iti.workshop.admin.presentation.utils.DataResponseState
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.products_product_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView:TextView = view.findViewById(R.id.testView)
        viewModel.getCountOfProducts()

        lifecycleScope.launch {
            viewModel.productCount.collect{states ->
                when(states){
                    is DataResponseState.OnError -> {
                        textView.text = states.message

                    }
                    is DataResponseState.OnLoading -> {
                        textView.text = "Loading.."

                    }
                    is DataResponseState.OnNothingData -> {}
                    is DataResponseState.OnSuccess -> {
                        textView.text = "Count is ${states.data.count}"
                    }
                }

            }
        }

    }

}