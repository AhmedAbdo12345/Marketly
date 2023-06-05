package iti.workshop.admin.data.remote.retrofit.product

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductImageCallApi {
    @GET("products/{product_id}/images/count.json")
    suspend fun getCountImageProduct(
        @Path("product_id") product_id: Long
    ): Response<iti.workshop.admin.data.dto.Count>

    @GET("products/{product_id}/images.json")
    suspend fun getImageProducts(
        @Path("product_id") product_id: Long
    ): Response<ImagesListResponse>

    @GET("products/{product_id}/images/{image_id}.json")
    suspend fun getImageProduct(
        @Path("product_id") product_id: Long,
        @Path("image_id") image_id: Long,
    ): Response<ImagesSingleResponse>

    @POST("products/{product_id}/images.json")
    suspend fun addImageProduct(
        @Path("product_id") product_id: Long,
        @Body data: PostImage
    ): Response<ImagesSingleResponse>

    @PUT("products/{product_id}/images/{image_id}.json")
    suspend fun updateImageProduct(
        @Path("product_id") product_id: Long,
        @Path("image_id") image_id: Long,
        @Body data: PostImage
    ): Response<ImagesSingleResponse>

    @DELETE("products/{product_id}/images/{image_id}.json")
    suspend fun deleteImageProduct(
        @Path("product_id") product_id: Long,
        @Path("image_id") image_id: Long,
    ): Response<Void>


}