package iti.mad.marketly.data.source.remote

import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import iti.mad.marketly.data.source.remote.retrofit.StripeApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StripeRemoteDataSource(var api:StripeApiClient):StripeRDataSourceInterface {
    override suspend fun createCustomer(): Flow<StripeCustomerResponse> = flow {
        emit(api.createCustomer())
    }

    override suspend fun createEphemeralKey(customerId: String): Flow<CreateEphemeralKeyResponse> = flow{
        emit(api.createEphemeralKey(customerId))
    }

    override suspend fun createPaymentIntent(
        customerId: String,
        amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean
    ): Flow<CreatePaymentIntentResponse> = flow {
        emit(api.createPaymentIntent(customerId,amount,currency,autoPaymentMethodsEnable))
    }
}