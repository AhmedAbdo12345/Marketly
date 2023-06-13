package iti.mad.marketly.data.repository.productRepository

import iti.mad.marketly.data.model.product.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    suspend fun getProducts(id: String) : Flow<ProductResponse>
}