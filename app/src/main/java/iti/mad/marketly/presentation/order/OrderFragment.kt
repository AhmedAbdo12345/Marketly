package iti.mad.marketly.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.api.adaptValue
import iti.mad.marketly.R
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.FragmentOrderBinding
import iti.mad.marketly.databinding.FragmentOrderDetailsBinding
import iti.mad.marketly.presentation.category.CategoryViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch

class OrderFragment : Fragment() ,OrderAdapter.ListItemClickListener{

lateinit var viewmodel: OrderViewmodel
lateinit var binding: FragmentOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel =
            ViewModelProvider(this, OrderViewmodel.Factory).get(OrderViewmodel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_order, container, false)

        binding = FragmentOrderBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getAllOrders()
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.orderResponse.collect{
                when (it) {
                    is ResponseState.OnSuccess -> {
                     var orderAdapter = OrderAdapter(this@OrderFragment)
                        orderAdapter.submitList(it.response)
                    binding.orderRecView.apply {
                        adapter = orderAdapter
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(context, 1).apply {
                            orientation = RecyclerView.VERTICAL
                        }
                    }
                    }

                    is ResponseState.OnError -> {}
                    else -> {}
                }
            }
        }

    }

    override fun onClickOrderDetailsButton(orderModel: OrderModel) {
        var action :OrderFragmentDirections.ActionOrderFragmentToOrderDetailsFragment =
            OrderFragmentDirections.actionOrderFragmentToOrderDetailsFragment(orderModel)
        findNavController().navigate(action)
    }
}