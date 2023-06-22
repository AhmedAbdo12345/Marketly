//package com.example.shopify.data.stripe.repository
//
//import iti.mad.marketly.data.source.remote.retrofit.StripeApiClient
//import com.example.shopify.di.IODispatcher
//import com.example.shopify.helpers.Resource
//import com.example.shopify.helpers.UIError
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//class StripeRepositoryImpl (
//    private val apiClient: StripeApiClient,
//     private val ioDispatcher: CoroutineDispatcher
//) : StripeRepository {
//
//    override suspend fun createCustomer(): String =
//        try {
//            val response = withContext(ioDispatcher) { apiClient.createCustomer().id }
//            Resource.Success(response)
//        } catch (e: Exception) {
//            Resource.Error(UIError.Unexpected)
//        }
//
//
//    override suspend fun createEphemeralKey(customerId: String): Resource<String> =
//        try {
//            val response = withContext(ioDispatcher) { apiClient.createEphemeralKey(customerId).id }
//            Resource.Success(response)
//        } catch (e: Exception) {
//            Resource.Error(UIError.Unexpected)
//        }
//
//
//    override suspend fun createPaymentIntent(
//        customerId: String,
//        amount: Int,
//        currency: String,
//        autoPaymentMethodsEnable: Boolean,
//    ): Resource<String> =
//        try {
//            val response = withContext(ioDispatcher) {
//                apiClient.createPaymentIntent(
//                    customerId,
//                    amount,
//                    currency,
//                    autoPaymentMethodsEnable
//                ).clientSecret
//            }
//            Resource.Success(response)
//        } catch (e: Exception) {
//            Resource.Error(UIError.Unexpected)
//        }
//
//}