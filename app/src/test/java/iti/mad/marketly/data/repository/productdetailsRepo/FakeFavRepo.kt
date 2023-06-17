package iti.mad.marketly.data.repository.productdetailsRepo

import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFavRepo : IFavouriteRepo {
    var favouriteProducts = mutableMapOf<String, MutableList<Product>>(
        "1" to mutableListOf<Product>(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 8391229473080,
                title = "ADIDAS | KID'S STAN SMITH",
                body_html = "The Stan Smith owned the tennis court in the '70s. Today it runs the streets with the same clean, classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "ADIDAS",
                isFavourite = true

            ), Product(
                id = 8391229473081,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE",
                isFavourite = true
            ), Product(
                id = 8391229473079,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        ),
        "2" to mutableListOf<Product>(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 8391229473081,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE",
                isFavourite = true
            ), Product(
                id = 8391229473079,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        ),
        "3" to mutableListOf<Product>(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 8391229473079,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        )
    )

    override suspend fun addProductToFavourite(userID: String, product: Product): Flow<Unit> =
        flow {

            if (favouriteProducts.containsKey(userID)) {

                if (favouriteProducts[userID] != null) {
                    if (!favouriteProducts[userID]?.contains(product)!!) favouriteProducts[userID]?.add(
                        product
                    )
                }

            } else {
                val productList = mutableListOf<Product>()
                productList.add(product)
                favouriteProducts[userID] = productList
            }
            emit(Unit)

        }

    override suspend fun isFavourite(product: Product, userID: String): Flow<Boolean> = flow {
        if (favouriteProducts.containsKey(userID)) {
            val isExist = favouriteProducts[userID]?.map {
                it.id
            }?.contains(product.id) ?: false
            emit(isExist)
        } else {
            emit(false)
        }
    }

    override suspend fun getAllFavourite(userID: String): Flow<FavouriteResponse> = flow {
        emit(FavouriteResponse(favouriteProducts[userID]!!))
    }

    override suspend fun getAllFavouriteIDS(userID: String): Flow<List<String>> = flow {
        val iDS = favouriteProducts[userID]?.map {
            it.id.toString()
        }
        emit(iDS!!)
    }

    override suspend fun deleteFromFavourite(userID: String, product: Product): Flow<Unit> = flow {
        if (favouriteProducts.containsKey(userID)) {
            if (favouriteProducts[userID]?.contains(product)!!) favouriteProducts[userID]?.remove(
                product
            )
        }
        emit(Unit)
    }
}