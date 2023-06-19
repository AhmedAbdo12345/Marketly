package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import iti.workshop.admin.data.local.ILocalDataSource
import iti.workshop.admin.data.local.room.ProductDao
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

class ImplProductRepository(private val _api: RetrofitInstance.ProductAPICalls, private val  _dao: ILocalDataSource) : IProductRepository {
    override fun getAllProducts(): Flow<MutableList<Product>>  = _dao.getAllProducts()

    override suspend fun insertAllTable(products: List<Product?>?) {
        _dao.insertAllTable(products)
    }

    // region Product
    override suspend fun getProduct(): Response<SuccessProductResponse> =
        _api.productCallApi.getProduct()

    override suspend fun addProduct(data: PostProduct): Response<Product> =
        _api.productCallApi.addProduct(data)

    override suspend fun updateProduct(id: Long, data: UpdateProduct): Response<Product> =
        _api.productCallApi.updateProduct(id, data)

    override suspend fun deleteProduct(id: Long): Response<Void> =
        _api.productCallApi.deleteProduct(id)

    override suspend fun getCount(): Response<Count>
     = try {
        _api.productCallApi.getCount()
    }catch (ex: RetrofitInstance.NoConnectivityException){
        Response.error(0, null)
    }


    // endregion Product

    // region Images
    override suspend fun getCountImageProduct(product_id: Long): Response<Count> =
        _api.productImageCallApi.getCountImageProduct(product_id)

    override suspend fun getImageProducts(product_id: Long): Response<ImagesListResponse>
    = _api.productImageCallApi.getImageProducts(product_id)
    override suspend fun getImageProduct(
        product_id: Long,
        image_id: Long
    ): Response<ImagesSingleResponse>
    = _api.productImageCallApi.getImageProduct(product_id, image_id)
    override suspend fun addImageProduct(
        product_id: Long,
        data: PostImage
    ): Response<ImagesSingleResponse>
    = _api.productImageCallApi.addImageProduct(product_id, data)

    override suspend fun updateImageProduct(
        product_id: Long,
        image_id: Long,
        data: PostImage
    ): Response<ImagesSingleResponse>
    = _api.productImageCallApi.updateImageProduct(product_id, image_id, data)

    override suspend fun deleteImageProduct(product_id: Long, image_id: Long): Response<Void>
    = _api.productImageCallApi.deleteImageProduct(product_id, image_id)

    // endregion Images

    // region Variants

    override suspend fun getCountProductVariant(product_id: Long): Response<Count>
            = _api.productVariantCallApi.getCountProductVariant(product_id)
    override suspend fun getProductVariants(product_id: Long): Response<VariantListResponse>
    = _api.productVariantCallApi.getProductVariants(product_id)

    override suspend fun getProductVariant(
        product_id: Long,
        variant_id: Long
    ): Response<VariantSingleResponseAndRequest>
    = _api.productVariantCallApi.getProductVariant(product_id, variant_id)

    override suspend fun addProductVariant(
        product_id: Long,
        data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>
    = _api.productVariantCallApi.addProductVariant(product_id, data)

    override suspend fun updateProductVariant(
        product_id: Long,
        variant_id: Long,
        data: VariantSingleResponseAndRequest
    ): Response<VariantSingleResponseAndRequest>
    = _api.productVariantCallApi.updateProductVariant(product_id, variant_id, data)

    override suspend fun deleteProductVariant(product_id: Long, variant_id: Long): Response<Void>
    = _api.productVariantCallApi.deleteProductVariant(product_id, variant_id)

    // endregion Variants
}