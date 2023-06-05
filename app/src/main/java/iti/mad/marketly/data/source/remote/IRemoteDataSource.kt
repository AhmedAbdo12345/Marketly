package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource{

    suspend fun registerUser(customerBody: CustomerBody):Flow<CustomerBody>
    suspend fun loginWithEmail(email:String): Flow<CustomerResponse>


}