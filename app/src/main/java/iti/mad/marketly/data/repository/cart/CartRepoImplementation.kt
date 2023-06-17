package iti.mad.marketly.data.repository.cart

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.settings.AddressData
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.flow.Flow

class CartRepoImplementation(val remote: IRemoteDataSource):CartRepoInterface {

    override  fun saveProductInOrder(orderModel: OrderModel) {
        remote.saveProductInOrder(orderModel)
    }
    override fun saveCartProduct(cartModel: CartModel) {
     remote.saveCartProduct(cartModel)
    }

    override suspend fun getAllCartProducts(): Flow<List<CartModel>> =remote.getAllCartProducts()
    override fun deleteCartItem(cartID: String) {
        remote.deleteCartItem(cartID)
    }

}