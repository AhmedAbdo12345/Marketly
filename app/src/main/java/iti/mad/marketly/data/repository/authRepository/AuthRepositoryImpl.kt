package iti.mad.marketly.data.repository.authRepository

import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow


class AuthRepositoryImpl(
    val remote: IRemoteDataSource,
) : IAuthRepository {
    override suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody> =
        remote.registerUser(customerBody)

    override suspend fun loginWithEmail(email: String): Flow<CustomerResponse> =
        remote.loginWithEmail(email)

}