package iti.workshop.admin.data.remote.retrofit.product

import iti.workshop.admin.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ProductVariantCallApi {

    @GET("products/{product_id}/variants/count.json")
    suspend fun getCountProductVariant(
        @Path("product_id") product_id: Long
    ): Response<iti.workshop.admin.data.dto.Count>

    @GET("products/{product_id}/variants.json")
    suspend fun getProductVariants(
        @Path("product_id") product_id: Long
    ): Response<VariantListResponse>

    @GET("products/{product_id}/variants/{variant_id}.json")
    suspend fun getProductVariant(
        @Path("product_id") product_id: Long,
        @Path("variant_id") variant_id: Long,
    ): Response<VariantSingleResponseAndRequest>

    @POST("products/{product_id}/variants.json")
    suspend fun addProductVariant(
        @Path("product_id") product_id: Long,
        @Body data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>

    @PUT("products/{product_id}/variants/{variant_id}.json")
    suspend fun updateProductVariant(
        @Path("product_id") product_id: Long,
        @Path("variant_id") variant_id: Long,
        @Body data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>

    @DELETE("products/{product_id}/variants/{variant_id}.json")
    suspend fun deleteProductVariant(
        @Path("product_id") product_id: Long,
        @Path("variant_id") variant_id: Long,
    ): Response<Void>
}