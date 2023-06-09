package iti.mad.marketly.data.repository.adsrepo

import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import kotlinx.coroutines.flow.Flow

interface AdsRepoInterface {

    suspend fun getDiscount(pricingRule:Long): Flow<DiscountResponce>
    suspend fun getPricingRules():Flow<PricingRules>
}