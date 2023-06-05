package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import iti.workshop.admin.data.remote.retrofit.ProductAPICalls
import retrofit2.Response

class ImplProductRepository(private val _api: ProductAPICalls) : IProductRepository {
    override suspend fun getProduct(): Response<SuccessProductResponse> =
        _api.productCallApi.getProduct()

    override suspend fun addProduct(data: PostProduct): Response<Product> =
        _api.productCallApi.addProduct(data)

    override suspend fun updateProduct(id: Long, data: UpdateProduct): Response<Product> =
        _api.productCallApi.updateProduct(id, data)

    override suspend fun deleteProduct(id: Long): Response<Void> =
        _api.productCallApi.deleteProduct(id)

    override suspend fun getCount(): Response<iti.workshop.admin.data.dto.Count> = _api.productCallApi.getCount()
}