package iti.mad.marketly.data.repository.productdetailsRepo

import iti.mad.marketly.data.model.productDetails.ProductDetails
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    suspend fun getProductDetails(id: Long): Flow<ProductDetails>
}