package iti.mad.marketly.data.repository.authRepository

import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepo : IAuthRepository {
    val customers = mutableListOf<CustomerBody>(
        CustomerBody(
            Customer(
                id = 1569788,
                first_name = "mahmoudsayed",
                email = "mahmoudsayed@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 16964,
                first_name = "mohamed arfa",
                email = "dev.arfa@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 987894,
                first_name = "hussein",
                email = "husseien.dd@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 987894,
                first_name = "ahmed abdo",
                email = "aboabdo@gmail.com",
                currency = "EGP"
            )
        )
    )

    override suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody> = flow {
        customers.add(customerBody)
        val customer = customers.find {
            it == customerBody
        }
        customer?.let {
            emit(customerBody)
        }
    }

    override suspend fun loginWithEmail(email: String): Flow<CustomerResponse> = flow {
        customers.find {
            it.customer?.email == email
        }.let {
            emit(CustomerResponse(listOf(it?.customer!!)))
        }
    }
}