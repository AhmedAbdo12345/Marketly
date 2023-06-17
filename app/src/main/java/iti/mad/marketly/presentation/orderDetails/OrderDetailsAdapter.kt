package iti.mad.marketly.presentation.orderDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.databinding.RvOrderBinding
import iti.mad.marketly.databinding.RvOrderDetailsBinding

class OrderDetailsAdapter (val orderModel: OrderModel) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    lateinit var binding: RvOrderDetailsBinding


    class OrderDetailsViewHolder(var binding: RvOrderDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvOrderDetailsBinding.inflate(inflate, parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (orderModel.itemList.size > 0){
            return orderModel.itemList.size
        }else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int)  {

         (orderModel.itemList[position].imageURL)?.let {
            Picasso.get().load(it).into(binding.imgOrderItem)
        }

        binding.tvOrderItemName.text =orderModel.itemList[position].title
        binding.tvOrderItemQuantity.text = "Quantity: ${orderModel.itemList[position].quantity}"
        binding.tvOderItemTotalPrice.text ="Total Price: ${(orderModel.itemList[position].price * orderModel.itemList[position].quantity)}"


    }
}