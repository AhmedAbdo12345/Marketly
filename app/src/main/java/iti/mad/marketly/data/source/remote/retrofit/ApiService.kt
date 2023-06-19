package iti.mad.marketly.data.source.remote.retrofit


import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.draftorder.DraftOrderBody
import iti.mad.marketly.data.model.draftorderresponse.DraftOrderResponse
import iti.mad.marketly.data.model.pricingrules.PricingRules
import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.settings.CurrencyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("customers.json")
    suspend fun registerUser(@Body customerBody: CustomerBody): CustomerBody

    @GET("customers/search.json")
    suspend fun loginWithEmail(@Query(value = "query") email: String): CustomerResponse

    //---------------------------------------------------------
    @GET("smart_collections.json")
    suspend fun getBrandsFromAPI(): BrandsResponse

    @GET("custom_collections.json")
    suspend fun getCategoryFromAPI(): CategoryResponse

    @GET("products.json")
    suspend fun getProductFromApi(@Query("collection_id") collection_id: String): ProductResponse

    /* @GET("collections/{brand_Id}/products.json\n")
     suspend fun getBrandProductFromAPI(@Path("brand_Id") brandID: String) : BrandProductResponse

     @GET("collections/{id}/products.json")
     suspend fun getCategoryProducts(@Path("id") id: Long): CategoryProductResponse*/
    //---------------------------------------------------------
    //Discounts
    @GET("price_rules/{pricing_rule}/discount_codes.json")
    suspend fun getDiscount(@Path("pricing_rule") pricingRule: Long): DiscountResponce

    //Pricing Rule
    @GET("price_rules.json")
    suspend fun getPricingRule(): PricingRules

    //Currency
    @GET("{apiKey}/latest/USD")
    suspend fun getExchangeRate(@Path("apiKey") apiKey: String): CurrencyResponse

    @GET("products/{id}.json")
    suspend fun getProductDetails(
        @Path("id") id: Long
    ): ProductDetails

    @GET("products.json")
    suspend fun getAllProducts(): ProductResponse
    //---------------------------------------------------------//
    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrderBody: DraftOrderBody):DraftOrderResponse
    @POST("{draft_order_id}/send_invoice.json")
    suspend fun sendInvoice(@Body invoice: DraftOrderInvoice,@Query("draft_order_id")draftID:String):DraftOrderInvoice
    @PUT("{draft_order_id}/complete.json")
    suspend fun completeOrder(@Query("draft_order_id")draftID:String):DraftOrderResponse
}