package iti.mad.marketly.data.repository.cart

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartRepo(private val fakeremote:IRemoteDataSource):CartRepoInterface {
//    private val cartList = mutableListOf<CartModel>(
//        CartModel(1,"",50,90.0,"item1")
//        , CartModel(2,"",40,100.0,"item2"), CartModel(3,"",90,120.0,"item3")
//    )
    override fun saveProductInOrder(orderModel: OrderModel) {
        fakeremote.saveProductInOrder(orderModel)
    }

    override fun saveCartProduct(cartModel: CartModel) {
        fakeremote.saveCartProduct(cartModel)
    }

    override suspend fun getAllCartProducts(): Flow<List<CartModel>> = fakeremote.getAllCartProducts()

    override fun deleteCartItem(cartID: String) {
        fakeremote.deleteCartItem(cartID)
    }

    override fun clearCart() {
        fakeremote.clearCart()
    }
}