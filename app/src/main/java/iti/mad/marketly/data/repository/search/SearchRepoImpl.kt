package iti.mad.marketly.data.repository.search

import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import iti.mad.marketly.data.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepoImpl(private  val remoteDataSource: IRemoteDataSource):SearchRepo {
    override suspend fun getAllProducts(): Flow<ProductResponse> = remoteDataSource.getAllProducts()

}