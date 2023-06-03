package iti.workshop.admin.data.remote.retrofit.coupon

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PriceRuleCallApi {
    @GET("price_rules/count.json")
    suspend fun getCountPriceRule(): Response<Count>

    @GET("price_rules.json")
    suspend fun getPriceRules(): Response<PriceRuleCodeListResponse>

    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun addPriceRule(
        @Path("price_rule_id") price_rule_id: Long,
        @Body data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>

    @PUT("price_rules/{price_rule_id}.json")
    suspend fun updatePriceRule(
        @Path("price_rule_id") price_rule_id: Long,
        @Body data: PriceRuleRequestAndResponse
    ): Response<PriceRuleRequestAndResponse>

    @DELETE("price_rules/{price_rule_id}.json")
    suspend fun deletePriceRule(@Path("price_rule_id") price_rule_id: Long): Response<Void>

}