package iti.mad.marketly.data.repository.favourite_repo

import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.productDetails.Product
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FavouriteRep(private val remoteDataSource: IRemoteDataSource) : IFavouriteRepo {
    override suspend fun addProductToFavourite(userID: String, product: Product): Flow<Unit> =
        remoteDataSource.addProductToFavourite(userID, product)

    override suspend fun isFavourite(product: Product, userID: String): Flow<Boolean> =
        remoteDataSource.isFavourite(product, userID)

    override suspend fun getAllFavourite(userID: String): Flow<FavouriteResponse> =
        remoteDataSource.getAllFavourite(userID)

    override suspend fun getAllFavouriteIDS(userID: String): Flow<List<String>> =
        remoteDataSource.getAllFavouriteIDS(userID)

    override suspend fun deleteFromFavourite(userID: String, product: Product): Flow<Unit> =
        remoteDataSource.deleteFromFavourite(userID, product)

}