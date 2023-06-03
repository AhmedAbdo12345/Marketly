package iti.workshop.admin.data.remote.retrofit.product

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ProductCallApi {
    @GET("products.json")
    suspend fun getProduct():Response<SuccessProductResponse>

    @POST("products.json")
    suspend fun addProduct(@Body data:PostProduct):Response<Product>

    @PUT("products/{id}.json")
    suspend fun updateProduct(@Path("id") id:Long,@Body data: UpdateProduct):Response<Product>

    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") id:Long):Response<Void>

    @GET("products/count.json")
    suspend fun getCount(): Response<Count>
}