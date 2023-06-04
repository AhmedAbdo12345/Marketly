package iti.workshop.admin.presentation.features.product.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import iti.workshop.admin.data.dto.Product

class ProductsDiffCallback: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean
    = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem.id == newItem.id &&
        oldItem.title == newItem.title



}
