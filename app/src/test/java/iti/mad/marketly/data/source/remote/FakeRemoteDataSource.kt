package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.brands.Rule
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.data.model.category.Image
import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource(
    val smartList: List<SmartCollection> = mutableListOf(),
    val customList: List<CustomCollection> = mutableListOf(),
    val orderList: List<OrderModel> = mutableListOf(),
    val  productRespnse: MutableMap<String, MutableList<Product>> = mutableMapOf()
) : IRemoteDataSource {
    val products = listOf<Product>(
        Product(
            id = 8391229473078,
            title = "MEN'S SHOES SMITH",
            body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "SMITH"
        ), Product(
            id = 8391229473080,
            title = "ADIDAS | KID'S STAN SMITH",
            body_html = "The Stan Smith owned the tennis court in the '70s. Today it runs the streets with the same clean, classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "ADIDAS"
        ), Product(
            id = 8391229473081,
            title = "ADIDAS | AL AHLY T-shirt",
            body_html = "The t shirt of mohamed abu treika",
            vendor = "NIKE"
        ), Product(
            id = 8391229473079,
            title = "11 salah liverpool",
            body_html = "Mohamed salah teshirt",
            vendor = "LiverPOol"
        )

    )
    val customers = mutableListOf<CustomerBody>(
        CustomerBody(
            Customer(
                id = 1569788,
                first_name = "mahmoudsayed",
                email = "mahmoudsayed@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 16964,
                first_name = "mohamed arfa",
                email = "dev.arfa@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 987894,
                first_name = "hussein",
                email = "husseien.dd@gmail.com",
                currency = "EGP"
            )
        ), CustomerBody(
            Customer(
                id = 987894,
                first_name = "ahmed abdo",
                email = "aboabdo@gmail.com",
                currency = "EGP"
            )
        )
    )
    private var favouriteProducts = mutableMapOf(
        "1" to mutableListOf(
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
        "2" to mutableListOf(
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
        "3" to mutableListOf(
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


    override suspend fun getExchangeRate(): Flow<CurrencyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody> = flow {
        customers.add(customerBody)
        val customer = customers.find {
            it == customerBody
        }
        customer?.let {
            emit(customerBody)
        }
    }

    override suspend fun loginWithEmail(email: String): Flow<CustomerResponse> = flow {
        customers.find {
            it.customer?.email == email
        }.let {
            emit(CustomerResponse(listOf(it?.customer!!)))
        }
    }

    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> = flow {
        val product = products.find {
            it.id == id
        }
        emit(ProductDetails(product))
    }

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
        favouriteProducts[userID]?.let {
            emit(FavouriteResponse(it))
        }
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

    override suspend fun getAllAddresses(): Flow<List<Address>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCartProducts(): Flow<List<CartModel>> {
        TODO("Not yet implemented")
    }

    override fun deleteAddress(addressID: String) {
        TODO("Not yet implemented")
    }

    override fun saveAddress(address: Address) {
        TODO("Not yet implemented")
    }

    override fun saveCartProduct(cartModel: CartModel) {
        TODO("Not yet implemented")
    }

    override fun deleteCartItem(cartID: String) {
        TODO("Not yet implemented")
    }

    override fun saveProductInOrder(orderModel: OrderModel) {
        val orderList = mutableListOf<OrderModel>()
        orderList.add(orderModel)

    }

    override suspend fun getAllOrders(): Flow<List<OrderModel>> = flow {
      // var orderModelList = listOf<OrderModel>(OrderModel())
        emit(orderList)
    }

    override suspend fun getAllProducts(): Flow<ProductResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun getBrands(): Flow<BrandsResponse> = flow {

        var brandResponse = BrandsResponse(smartList)

        emit(brandResponse)
    }

    override suspend fun getProducts(id: String): Flow<ProductResponse> = flow {
            emit(ProductResponse(productRespnse[id]!!))
    }

    override suspend fun getCategory(): Flow<CategoryResponse> = flow {

        var categoryResponse = CategoryResponse(customList)
        emit(categoryResponse)
    }


}