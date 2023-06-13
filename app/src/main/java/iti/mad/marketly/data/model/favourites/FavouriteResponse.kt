package iti.mad.marketly.data.model.favourites

import iti.mad.marketly.data.model.product.Product


data class FavouriteResponse(
    val products: List<Product>
)