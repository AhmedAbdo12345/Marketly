package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import retrofit2.http.GET
import retrofit2.http.Path

interface CallApi {
    @GET("smart_collections.json")
    suspend fun getBrandsFromAPI() : BrandsResponse

    @GET("collections/{brand_Id}/products.json\n")
    suspend fun getBrandProductFromAPI(@Path("brand_Id") brandID: String) : BrandProductResponse

    //Discounts
    @GET("price_rules/{pricing_rule}/discount_codes.json")
    suspend fun getDiscount(@Path("pricing_rule")pricingRule:Long): DiscountResponce
    //Pricing Rule
    @GET("price_rules.json")
    suspend fun getPricingRule():PricingRules
}