package iti.mad.marketly.data.repository.order

import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeOrderRepo(val remoteDataSource: FakeRemoteDataSource): OrderRepo {
    override suspend fun getAllOrders(): Flow<List<OrderModel>> {
        return remoteDataSource.getAllOrders()
    }
}