package iti.mad.marketly.data.repository.authRepository

import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import kotlinx.coroutines.flow.Flow


interface IAuthRepository{

    suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody>
    suspend fun loginWithEmail(email:String): Flow<CustomerResponse>
}