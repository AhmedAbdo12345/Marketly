package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.pricingrules.PricingRules

import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.product.ProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

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
    //-------------------------------------------------------
    suspend fun getAllProducts(): Flow<ProductResponse>
    //---------------Brands----------------------------------------
    suspend fun getBrands(): Flow<BrandsResponse>
    //------------------Products----------------------------------------------
    suspend fun getProducts(id: String) : Flow<ProductResponse>
   //--------------------Category------------------------------------------------
   suspend fun getCategory(): Flow<CategoryResponse>
    suspend fun getDiscount(pricingRule:Long): Flow<DiscountResponce>
    suspend fun getPricingRules():Flow<PricingRules>
    fun clearCart()
}