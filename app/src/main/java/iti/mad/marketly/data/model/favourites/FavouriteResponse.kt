package iti.mad.marketly.data.model.favourites

import iti.mad.marketly.data.model.productDetails.Product


data class FavouriteResponse(
    val products: List<Product>
)