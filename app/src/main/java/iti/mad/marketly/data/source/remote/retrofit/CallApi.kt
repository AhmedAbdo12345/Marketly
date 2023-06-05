package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.model.CustomerResponse
import retrofit2.http.Body
import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


import retrofit2.http.Path

interface CallApi {
    @POST("customers.json")
    suspend fun registerUser(@Body customerBody: CustomerBody): CustomerBody
    @GET("customers.json")
    suspend fun loginWithEmail(@Query(value = "email:") email:String):CustomerResponse

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