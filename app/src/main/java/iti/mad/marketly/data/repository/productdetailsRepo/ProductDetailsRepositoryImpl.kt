package iti.mad.marketly.data.repository.productdetailsRepo

import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductDetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) :
    ProductDetailsRepository {
    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> =
        remoteDataSource.getProductDetails(id)

}