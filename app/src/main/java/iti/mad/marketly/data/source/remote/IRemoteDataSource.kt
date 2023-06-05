package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.model.CustomerResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface IRemoteDataSource{

    suspend fun registerUser(customerBody: CustomerBody):Flow<CustomerBody>
    suspend fun loginWithEmail(email:String): Flow<CustomerResponse>


}