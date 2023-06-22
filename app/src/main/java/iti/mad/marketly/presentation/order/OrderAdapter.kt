package iti.mad.marketly.presentation.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.RvOrderBinding
import iti.mad.marketly.presentation.categoryProduct.CategoryProductDiffUtils
import iti.mad.marketly.utils.CurrencyConverter
import iti.mad.marketly.utils.SettingsManager

class OrderAdapter (var mClickListener: ListItemClickListener) : ListAdapter<OrderModel, OrderAdapter.AdapterViewHolder>(
    OrderDiffUtils()
) {

    lateinit var binding: RvOrderBinding

    interface ListItemClickListener {
        fun onClickOrderDetailsButton(orderModel: OrderModel)
    }
    class AdapterViewHolder(var binding: RvOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvOrderBinding.inflate(inflate, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int)  {
val order=getItem(position)
        var quant = 0
        var totalPrice = 0.0
        binding.orderModel = getItem(position)
        binding.action = mClickListener
binding.tvOrderId.text = getItem(position).orderID
        binding.tvOrderDate.text = getItem(position).date
        //binding.tvOrderTotalPrice.text = "${getItem(position).orderTotalPrice} $"
        if(SettingsManager.getCurrncy()=="USD"){
            binding.tvOrderTotalPrice.text = CurrencyConverter.switchToUSD(getItem(position).orderTotalPrice.toString(),binding.tvOrderTotalPrice.id)+" $"

        }else{
            binding.tvOrderTotalPrice.text = CurrencyConverter.switchToEGP(getItem(position).orderTotalPrice.toString(),binding.tvOrderTotalPrice.id)+" LE"

        }
        //binding.tvOrderQuantity.text = getItem(position).itemList.get(position-1).quantity.toString()
        for (item in order.itemList){
            quant+=item.quantity.toInt()
        }
        binding.tvOrderQuantity.text = order.itemCount.toString()
    }
}