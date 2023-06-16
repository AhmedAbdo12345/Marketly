package iti.mad.marketly.data.repository.order

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow

class OrderRepoImpl(val remote: IRemoteDataSource) : OrderRepo {


    override suspend fun getAllOrders(): Flow<List<OrderModel>> = remote.getAllOrders()

}