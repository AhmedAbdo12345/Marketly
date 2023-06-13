package iti.mad.marketly.presentation.order

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.product.Product

class OrderDiffUtils  : DiffUtil.ItemCallback<Product>()  {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}