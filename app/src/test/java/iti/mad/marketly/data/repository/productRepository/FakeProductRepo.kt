package iti.mad.marketly.data.repository.productRepository

import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeProductRepo(val remoteDataSource: FakeRemoteDataSource): ProductRepo {
    override suspend fun getProducts(id: String): Flow<ProductResponse> {
        return  remoteDataSource.getProducts(id)
    }
}