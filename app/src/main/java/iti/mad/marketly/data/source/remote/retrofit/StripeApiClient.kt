package iti.mad.marketly.data.source.remote.retrofit

import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface StripeApiClient {

    companion object {
        const val BASE_URL = "https://api.stripe.com/"
        private const val SECRET = "sk_test_51NLhznEtHa5WFOrtbEw0ypZnuF7QsNsd3dLrSV4RypkikiYMBBZfEvfVffNLUoRJrLYuFp7Q5vla5KuMN6aJ1ev700Ar3vN66U"
    }


    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-11-15"
    )
    @POST("v1/customers")
    suspend fun createCustomer(): StripeCustomerResponse

    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-11-15"
    )
    @POST("v1/ephemeral_keys")
    suspend fun createEphemeralKey(
        @Query("customer") customerId: String
    ): CreateEphemeralKeyResponse


    @Headers(
        "Authorization: Bearer $SECRET"
    )
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Query("customer") customerId: String,
        @Query("amount") amount: Int,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") autoPaymentMethodsEnable: Boolean,
    ): CreatePaymentIntentResponse

}