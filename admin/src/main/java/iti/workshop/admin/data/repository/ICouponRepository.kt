package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ICouponRepository {

    suspend fun getCount(): Response<Count>

    suspend fun getDiscounts(
        price_rule_id: Long,
    ): Response<DiscountCodeListResponse>

    suspend fun addDiscount(
        price_rule_id: Long,
        data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>

    suspend fun updateDiscount(
        price_rule_id: Long,
        discount_code_id: Long,
        data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>

    suspend fun deleteDiscount(id: Long): Response<Void>




    suspend fun getCountPriceRule(): Response<Count>

    suspend fun getPriceRules(): Response<PriceRuleCodeListResponse>

    suspend fun addPriceRule(
        price_rule_id: Long,
        data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>

    suspend fun updatePriceRule(
        price_rule_id: Long,
        data: PriceRuleRequestAndResponse
    ): Response<PriceRuleRequestAndResponse>

    suspend fun deletePriceRule(price_rule_id: Long): Response<Void>

}