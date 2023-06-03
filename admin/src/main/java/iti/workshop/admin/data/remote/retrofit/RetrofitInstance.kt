package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.Constants
import iti.workshop.admin.data.remote.retrofit.coupon.DiscountCodeCallApi
import iti.workshop.admin.data.remote.retrofit.coupon.PriceRuleCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryItemCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryLevelCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryLocationCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductImageCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductVariantCallApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

data class APICalls(
    // Coupon
    val discountCodeCallApi: DiscountCodeCallApi = RetrofitInstance.discountCodeCallApi,
    val priceRuleCallApi:PriceRuleCallApi = RetrofitInstance.priceRuleCallApi,

    // Inventory
    val inventoryItemCallApi: InventoryItemCallApi = RetrofitInstance.inventoryItemCallApi,
    val inventoryLevelCallApi:InventoryLevelCallApi = RetrofitInstance.inventoryLevelCallApi,
    val inventoryLocationCallApi:InventoryLocationCallApi = RetrofitInstance.inventoryLocationCallApi,

    // Product
    val productCallApi: ProductCallApi = RetrofitInstance.productCallApi,
    val productImageCallApi:ProductImageCallApi = RetrofitInstance.productImageCallApi,
    val productVariantCallApi:ProductVariantCallApi = RetrofitInstance.productVariantCallApi

)
object RetrofitInstance {

    private val retrofit: Retrofit  =  Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(loggerInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .client(headerConfiguration())
        .build()

    // Coupon
    val discountCodeCallApi: DiscountCodeCallApi = retrofit.create(DiscountCodeCallApi::class.java)
    val priceRuleCallApi:PriceRuleCallApi = retrofit.create(PriceRuleCallApi::class.java)

    // Inventory
    val inventoryItemCallApi: InventoryItemCallApi = retrofit.create(InventoryItemCallApi::class.java)
    val inventoryLevelCallApi:InventoryLevelCallApi = retrofit.create(InventoryLevelCallApi::class.java)
    val inventoryLocationCallApi:InventoryLocationCallApi = retrofit.create(InventoryLocationCallApi::class.java)

    // Product
    val productCallApi: ProductCallApi = retrofit.create(ProductCallApi::class.java)
    val productImageCallApi:ProductImageCallApi = retrofit.create(ProductImageCallApi::class.java)
    val productVariantCallApi:ProductVariantCallApi = retrofit.create(ProductVariantCallApi::class.java)








    private fun loggerInterceptor(): OkHttpClient {
        // Logging Retrofit
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private fun headerConfiguration():OkHttpClient{
        return  OkHttpClient.Builder().apply {
            addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-Shopify-Access-Token", Constants.API_KEY)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                val request = requestBuilder.build()
                chain.proceed(request)
            })
        }.build()
    }



}