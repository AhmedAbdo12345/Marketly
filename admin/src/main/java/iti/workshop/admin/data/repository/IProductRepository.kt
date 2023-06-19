package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IProductRepository {

    fun getAllProducts(): Flow<MutableList<Product>>

    suspend fun insertAllTable(products: List<Product?>?)
    // region Product
    suspend fun getProduct(): Response<SuccessProductResponse>

    suspend fun addProduct(data: PostProduct): Response<Product>

    suspend fun updateProduct(id:Long, data: UpdateProduct): Response<Product>

    suspend fun deleteProduct(id:Long): Response<Void>

    suspend fun getCount(): Response<iti.workshop.admin.data.dto.Count>

    // endregion Product

    // region Images
    suspend fun getCountImageProduct(product_id: Long
    ): Response<Count>

    suspend fun getImageProducts(
        product_id: Long
    ): Response<ImagesListResponse>

    suspend fun getImageProduct(
        product_id: Long,
        image_id: Long,
    ): Response<ImagesSingleResponse>

    suspend fun addImageProduct(
        product_id: Long,
        data: PostImage
    ): Response<ImagesSingleResponse>

    suspend fun updateImageProduct(
        product_id: Long,image_id: Long,data: PostImage
    ): Response<ImagesSingleResponse>

    suspend fun deleteImageProduct(product_id: Long,image_id: Long,
    ): Response<Void>
    // endregion Images

    // region Variants
    suspend fun getCountProductVariant(product_id: Long): Response<Count>

    suspend fun getProductVariants(product_id: Long): Response<VariantListResponse>

    suspend fun getProductVariant(
        product_id: Long,variant_id: Long,
    ): Response<VariantSingleResponseAndRequest>

    suspend fun addProductVariant(
        product_id: Long,
        data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>

    suspend fun updateProductVariant(
        product_id: Long,
        variant_id: Long,
        data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>

    suspend fun deleteProductVariant(product_id: Long,variant_id: Long,
    ): Response<Void>
    // endregion Variants
}