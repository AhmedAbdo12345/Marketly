package iti.mad.marketly.data.repository.productRepository

import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepoImpl(val apiService: ApiService) : ProductRepo{
    override suspend fun getProducts(id:String): Flow<ProductResponse> = flow{
        emit(apiService.getProductFromApi(id))
    }

}