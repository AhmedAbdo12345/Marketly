package iti.mad.marketly.presentation.orderDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.databinding.FragmentOrderDetailsBinding
import iti.mad.marketly.presentation.brandProduct.BrandProductFragmentArgs
import iti.mad.marketly.presentation.order.OrderFragment


class OrderDetailsFragment : Fragment() {

lateinit var binding: FragmentOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_order_details, container, false)
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).orderModel
        binding.tvOrderDetailsId.text= order.orderID
        binding.tvOrderDetailsQuantity.text = order.itemCount.toString()
        binding.tvOrderDetailsTotalPrice.text= "${order.orderTotalPrice}"
        binding.tvOrderDetailsDate.text = order.date


        var orderDetailsAdapter = OrderDetailsAdapter(order)


        binding.orderDetailsRecView.apply {
            adapter = orderDetailsAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

    }
}