package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.dto.PostProduct
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.SuccessProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductCallApi {
    @GET("products.json")
    suspend fun getProduct():Response<SuccessProductResponse>

    @POST("products.json")
    suspend fun addProduct(@Body data:PostProduct):Response<Product>

}