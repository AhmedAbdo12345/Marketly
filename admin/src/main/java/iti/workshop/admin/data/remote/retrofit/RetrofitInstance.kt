package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit  =  Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(loggerInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .client(headerConfiguration())
        .build()

    val couponCallApi:CouponCallApi = retrofit.create(CouponCallApi::class.java)

    val inventoryCallApi:InventoryCallApi  = retrofit.create(InventoryCallApi::class.java)

    val productCallApi:ProductCallApi = retrofit.create(ProductCallApi::class.java)



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