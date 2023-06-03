package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit  =  Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(cashAndLoggerManager())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val couponCallApi:CouponCallApi = retrofit.create(CouponCallApi::class.java)

    val inventoryCallApi:InventoryCallApi  = retrofit.create(InventoryCallApi::class.java)

    val productCallApi:ProductCallApi = retrofit.create(ProductCallApi::class.java)



    private fun cashAndLoggerManager(): OkHttpClient {
        // Logging Retrofit
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }



}