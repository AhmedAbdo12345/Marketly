package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.model.CustomerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CallApi {
    @POST("customers.json")
    suspend fun registerUser(@Body customerBody: CustomerBody): CustomerBody
    @GET("customers.json")
    suspend fun loginWithEmail(@Query(value = "email:") email:String):CustomerResponse

}