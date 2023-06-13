package iti.mad.marketly.data.repository.cart

import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.settings.Address
import kotlinx.coroutines.flow.Flow

interface CartRepoInterface {
    fun saveCartProduct(product: Product)
    suspend fun getAllCartProducts(): Flow<List<Product>>
}