package iti.mad.marketly.data.model.categoryProduct

import iti.mad.marketly.data.model.brandproduct.Product

data class CategoryProductResponse(
    val products: List<Product>
)