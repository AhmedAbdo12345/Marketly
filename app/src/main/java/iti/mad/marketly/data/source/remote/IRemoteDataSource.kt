package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.model.CustomerResponse
import iti.mad.marketly.data.model.productDetails.ProductDetails
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {

    suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody>
    suspend fun loginWithEmail(email: String): Flow<CustomerResponse>
    suspend fun getProductDetails(id: Long): Flow<ProductDetails>

}