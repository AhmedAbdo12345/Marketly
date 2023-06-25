package iti.mad.marketly.data.repository.stripe

import com.example.shopify.data.stripe.repository.StripeRepository
import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import iti.mad.marketly.data.source.remote.StripeRDataSourceInterface
import kotlinx.coroutines.flow.Flow

class StripeRepoImplementation(val stripe:StripeRDataSourceInterface):StripeRepository {
    override suspend fun createCustomer(): Flow<StripeCustomerResponse> = stripe.createCustomer()

    override suspend fun createEphemeralKey(customerId: String): Flow<CreateEphemeralKeyResponse>
    = stripe.createEphemeralKey(customerId)

    override suspend fun createPaymentIntent(
        customerId: String,
        amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean
    ): Flow<CreatePaymentIntentResponse> = stripe.createPaymentIntent(customerId,amount,currency,autoPaymentMethodsEnable)
}