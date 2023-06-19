package iti.workshop.admin.data.remote.remoteDataSource

import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.remote.retrofit.coupon.DiscountCodeCallApi
import iti.workshop.admin.data.remote.retrofit.coupon.PriceRuleCallApi

data class CouponAPICalls(
    // Coupon
    val discountCodeCallApi: DiscountCodeCallApi = RetrofitInstance.discountCodeCallApi,
    val priceRuleCallApi: PriceRuleCallApi = RetrofitInstance.priceRuleCallApi,
)