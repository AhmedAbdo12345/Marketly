package iti.mad.marketly.data.repository.order

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderRepo {
    suspend fun getAllOrders(): Flow<List<OrderModel>>
    suspend fun getProductsOfOrder(): Flow<List<CartModel>>

}