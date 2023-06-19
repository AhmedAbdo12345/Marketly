package iti.mad.marketly.data.repository.search

import iti.mad.marketly.data.model.product.ProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SearchRepo {
    suspend fun getAllProducts(): Flow<ProductResponse>
}