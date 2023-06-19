package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import iti.workshop.admin.data.remote.remoteDataSource.CouponAPICalls
import retrofit2.Response

class ImplCouponRepository(private val _api: CouponAPICalls):ICouponRepository {
    override suspend fun getCount(): Response<Count> =_api.discountCodeCallApi.getCount()

    override suspend fun getDiscounts(price_rule_id: Long): Response<DiscountCodeListResponse>
    = _api.discountCodeCallApi.getDiscounts(price_rule_id)

    override suspend fun addDiscount(
        data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>
    = _api.discountCodeCallApi.addDiscount(data.discount_code.price_rule_id,data)

    override suspend fun updateDiscount(
        price_rule_id: Long,
        discount_code_id: Long,
        data: DiscountCodeRequestAndResponse
    ): Response<DiscountCodeRequestAndResponse>
     = _api.discountCodeCallApi.updateDiscount(price_rule_id, discount_code_id, data)
    override suspend fun deleteDiscount(price_rule_id: Long,discount_code_id: Long): Response<Void>
     = _api.discountCodeCallApi.deleteDiscount(price_rule_id,discount_code_id)

    override suspend fun getCountPriceRule(): Response<Count>
     = _api.priceRuleCallApi.getCountPriceRule()

    override suspend fun getPriceRules(): Response<PriceRuleCodeListResponse>
     = _api.priceRuleCallApi.getPriceRules()

    override suspend fun addPriceRule(
        data: PriceRuleRequestAndResponse
    ): Response<PriceRuleRequestAndResponse>
     = _api.priceRuleCallApi.addPriceRule(data)

    override suspend fun updatePriceRule(
        price_rule_id: Long,
        data: PriceRuleRequestAndResponse
    ): Response<PriceRuleRequestAndResponse>
    = _api.priceRuleCallApi.updatePriceRule(price_rule_id,data)

    override suspend fun deletePriceRule(price_rule_id: Long): Response<Void>
    = _api.priceRuleCallApi.deletePriceRule(price_rule_id)
}