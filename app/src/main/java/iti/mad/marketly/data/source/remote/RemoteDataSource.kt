package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource(
    private val api: ApiService
) : IRemoteDataSource {
    override suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody> = flow {
        emit(api.registerUser(customerBody))
    }

    override suspend fun loginWithEmail(email: String): Flow<CustomerResponse> = flow {
        emit(api.loginWithEmail(email))
    }

}