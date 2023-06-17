package iti.mad.marketly.presentation.order

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product

class OrderDiffUtils  : DiffUtil.ItemCallback<OrderModel>()  {
    override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
        return oldItem.orderID == newItem.orderID
    }

    override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
        return oldItem == newItem
    }
}