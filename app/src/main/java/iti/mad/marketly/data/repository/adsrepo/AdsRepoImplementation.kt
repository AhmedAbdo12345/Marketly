package iti.mad.marketly.data.repository.adsrepo

import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdsRepoImplementation(val remote: IRemoteDataSource):AdsRepoInterface{
    override suspend fun getDiscount(pricingRule:Long): Flow<DiscountResponce> = remote.getDiscount(pricingRule)



    override suspend fun getPricingRules(): Flow<PricingRules> = remote.getPricingRules()
}