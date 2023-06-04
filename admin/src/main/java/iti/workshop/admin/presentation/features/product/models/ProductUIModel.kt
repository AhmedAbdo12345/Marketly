package iti.workshop.admin.presentation.features.product.models

import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.Product

data class ProductUIModel(
    val count: Count?,
    val products: List<Product>?
)
