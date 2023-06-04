package iti.workshop.admin.presentation.features.product.ui.adapters

import iti.workshop.admin.data.dto.Product

class ItemOnCLickListener(
    val clickListener: (product: Product) -> Unit,
    val deleteItemListener: (product: Product) -> Unit
) {
    fun onClick(product: Product) = clickListener(product)
    fun onDeleteItem(product: Product) = deleteItemListener(product)
}