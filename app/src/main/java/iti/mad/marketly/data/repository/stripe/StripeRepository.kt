package com.example.shopify.data.stripe.repository

import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import kotlinx.coroutines.flow.Flow


interface StripeRepository {
    suspend fun createCustomer(): Flow<StripeCustomerResponse>
    suspend fun createEphemeralKey(customerId: String): Flow<CreateEphemeralKeyResponse>
    suspend fun createPaymentIntent(
        customerId: String,
        amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean
    ): Flow<CreatePaymentIntentResponse>
}