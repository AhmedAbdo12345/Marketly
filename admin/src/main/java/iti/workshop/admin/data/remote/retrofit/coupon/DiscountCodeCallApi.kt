package iti.workshop.admin.data.remote.retrofit.coupon

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface DiscountCodeCallApi {
    @GET("discount_codes/count.json")
    suspend fun getCount(): Response<Count>

    @GET("products.json")
    suspend fun getDiscounts(): Response<SuccessProductResponse>

    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun addDiscount(@Body data: DiscountCodeRequestAndResponse): Response<DiscountCodeRequestAndResponse>

    @PUT("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun updateDiscount(
        @Path("price_rule_id") price_rule_id: Long,
        @Path("discount_code_id") discount_code_id: Long,
        @Body data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>

    @DELETE("discount_codes/{id}.json")
    suspend fun deleteDiscount(@Path("id") id: Long): Response<Void>

}