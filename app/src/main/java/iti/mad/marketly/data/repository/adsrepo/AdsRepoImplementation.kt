package iti.mad.marketly.data.repository.adsrepo

import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import iti.mad.marketly.data.source.remote.retrofit.CallApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdsRepoImplementation(val api:CallApi):AdsRepoInterface{
    override suspend fun getDiscount(pricingRule:Long): Flow<DiscountResponce> = flow {
        emit(api.getDiscount(pricingRule))
    }

    override suspend fun getPricingRules(): Flow<PricingRules> = flow {
        emit(api.getPricingRule())
    }
}