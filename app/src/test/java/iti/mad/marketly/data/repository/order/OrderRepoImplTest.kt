package iti.mad.marketly.data.repository.order

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderRepoImplTest {
    var cartModelList = listOf<CartModel>(
        CartModel(
            1515,
            "https://cdn.shopify.com/s/files/1/0775/1142/6358/products/7883dc186e15bf29dad696e1e989e914.jpg",
            2,
            5.6,
            "AA"
        ), CartModel(
            1893,
            "https://cdn.shopify.com/s/files/1/0775/1142/6358/products/7883dc186e15bf29dad696e1e989e914.jpg",
            3,
            7.2,
            "BB"
        )
    )

    var orderModelList = listOf<OrderModel>(
        OrderModel(
            "115200",
            cartModelList, 3, "22-6-2023", 1500.0
        ), OrderModel(
            "841515",
            cartModelList, 8, "25-6-2023", 950.0
        )
    )
    lateinit var remoteDataSource: FakeRemoteDataSource
    lateinit var orderRepo: OrderRepo

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(orderList = orderModelList)
        orderRepo = OrderRepoImpl(remoteDataSource)
    }

    @Test
    fun getAllOrders_NoInput_checkOrderIDNumberForFirstOrder() = runBlocking {

        // Given
        // When
        var result = orderRepo.getAllOrders()
        // Then
        result.collect{
            assertThat(it[0].orderID, CoreMatchers.`is`("115200"))
        }
    }
}