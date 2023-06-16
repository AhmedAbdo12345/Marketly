package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.order.OrderModel

import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.model.product.Product
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {


    suspend fun getExchangeRate(): Flow<CurrencyResponse>
    suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody>
    suspend fun loginWithEmail(email: String): Flow<CustomerResponse>
    suspend fun getProductDetails(id: Long): Flow<ProductDetails>
    suspend fun addProductToFavourite(userID:String,product: Product):Flow<Unit>
    suspend fun isFavourite(product: Product,userID: String):Flow<Boolean>
    suspend fun getAllFavourite(userID: String):Flow<FavouriteResponse>
    suspend fun getAllFavouriteIDS(userID: String):Flow<List<String>>
    suspend fun deleteFromFavourite(userID: String,product: Product):Flow<Unit>
    suspend fun getAllAddresses():Flow<List<Address>>
    suspend fun getAllCartProducts():Flow<List<CartModel>>
    fun deleteAddress(addressID:String)
    fun saveAddress(address: Address)
    fun saveCartProduct(cartModel: CartModel)
    fun deleteCartItem(cartID:String)
    //-------------------------------------------------------
     fun saveProductInOrder(orderModel: OrderModel)
    suspend fun getAllOrders(): Flow<List<OrderModel>>
    suspend fun getProductsOfOrder(): Flow<List<CartModel>>
    //-------------------------------------------------------
}