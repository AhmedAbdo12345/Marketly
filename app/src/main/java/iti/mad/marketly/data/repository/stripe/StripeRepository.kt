package com.example.shopify.data.stripe.repository


interface StripeRepository {
    suspend fun createCustomer(): String
    suspend fun createEphemeralKey(customerId: String): String
    suspend fun createPaymentIntent(
        customerId: String,
        amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean
    ): String
}