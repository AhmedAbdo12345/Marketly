package iti.workshop.admin.presentation.features.product.ui.adapters

import iti.workshop.admin.data.dto.Product

class ItemOnCLickListener(
    val clickListener: (product: Product) -> Unit,
    val saveFavListener: (product: Product) -> Unit
) {
    fun onClick(product: Product) = clickListener(product)
    fun onSaveFav(product: Product) = saveFavListener(product)
}