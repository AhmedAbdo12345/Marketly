package iti.mad.marketly.data.repository.favourite_repo

import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.productDetails.Product
import kotlinx.coroutines.flow.Flow

interface IFavouriteRepo {
    suspend fun addProductToFavourite(userID:String,product: Product): Flow<Unit>
    suspend fun isFavourite(product: Product,userID: String):Flow<Boolean>
    suspend fun getAllFavourite(userID: String):Flow<FavouriteResponse>
    suspend fun getAllFavouriteIDS(userID: String):Flow<List<String>>
    suspend fun deleteFromFavourite(userID: String,product: Product):Flow<Unit>

}