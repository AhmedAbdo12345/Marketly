package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {


    private val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Shopify-Access-Token", Constants.API_ACCESS_TOKEN)
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    val currncyApi: ApiService by lazy {
        currencyRetrofit.create(ApiService::class.java)
    }
private val currencyRetrofit:Retrofit by lazy {
    val httpCurrency = OkHttpClient.Builder().build()
    Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_CUR)
        .client(httpCurrency)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}

    private fun cashAndLoggerManager(): OkHttpClient {
        // Logging Retrofit
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }



}