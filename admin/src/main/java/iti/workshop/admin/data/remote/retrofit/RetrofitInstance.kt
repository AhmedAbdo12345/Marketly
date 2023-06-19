package iti.workshop.admin.data.remote.retrofit

import android.content.Context
import android.net.ConnectivityManager
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
import java.io.IOException


class RetrofitInstance(private val context: Context) {
    private val headerConfiguration:OkHttpClient
            = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-Shopify-Access-Token", iti.workshop.admin.data.Constants.API_KEY)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        })
    }.build()


    private val loggerInterceptor:OkHttpClient by lazy {
        // Logging Retrofit
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }


    private val checkConnectivity = OkHttpClient.Builder()
        .addInterceptor { chain ->
            if (!isInternetAvailable(context)) {
                throw NoConnectivityException()
            }
            chain.proceed(chain.request())
        }
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(checkConnectivity)
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggerInterceptor)
        .client(headerConfiguration)
        .build()

    // Coupon
    inner class CouponAPICalls() {
        val discountCodeCallApi: DiscountCodeCallApi =
            retrofit.create(DiscountCodeCallApi::class.java)
        val priceRuleCallApi: PriceRuleCallApi = retrofit.create(PriceRuleCallApi::class.java)
    }
    // Inventory
    inner class InventoryAPICalls() {
        val inventoryItemCallApi: InventoryItemCallApi =
            retrofit.create(InventoryItemCallApi::class.java)
        val inventoryLevelCallApi: InventoryLevelCallApi =
            retrofit.create(InventoryLevelCallApi::class.java)
        val inventoryLocationCallApi: InventoryLocationCallApi =
            retrofit.create(InventoryLocationCallApi::class.java)
    }

    // Product
    inner class ProductAPICalls(){
        val productCallApi: ProductCallApi = retrofit.create(ProductCallApi::class.java)
        val productImageCallApi: ProductImageCallApi = retrofit.create(ProductImageCallApi::class.java)
        val productVariantCallApi: ProductVariantCallApi =
            retrofit.create(ProductVariantCallApi::class.java)
    }




    class NoConnectivityException: IOException() {
        override val message: String
            get() = "No internet connection"
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }




}