package iti.workshop.admin.data.remote.retrofit.product

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductImageCallApi{
    @GET("products.json")
    suspend fun getProduct(): Response<SuccessProductResponse>

    @POST("products.json")
    suspend fun addProduct(@Body data: PostProduct): Response<Product>

    @PUT("products/{id}.json")
    suspend fun updateProduct(@Path("id") id:Long, @Body data: UpdateProduct): Response<Product>

    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") id:Long): Response<Void>

    @GET("products/count.json")
    suspend fun getCount(): Response<Count>
}