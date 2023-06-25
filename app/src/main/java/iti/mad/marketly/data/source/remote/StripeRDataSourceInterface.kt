package iti.mad.marketly.data.source.remote

import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import kotlinx.coroutines.flow.Flow

interface StripeRDataSourceInterface {
    suspend fun createCustomer(): Flow<StripeCustomerResponse>
    suspend fun createEphemeralKey(
        customerId: String
    ): Flow<CreateEphemeralKeyResponse>
    suspend fun createPaymentIntent(
       customerId: String,
       amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean,
    ): Flow<CreatePaymentIntentResponse>
}