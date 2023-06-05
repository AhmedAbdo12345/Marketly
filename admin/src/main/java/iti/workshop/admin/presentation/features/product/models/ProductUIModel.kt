package iti.workshop.admin.presentation.features.product.models

import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.Product

data class ProductUIModel(
    val count: iti.workshop.admin.data.dto.Count?,
    val products: List<Product>?
)
