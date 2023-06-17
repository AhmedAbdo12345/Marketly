package iti.mad.marketly.presentation.order

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.repository.order.FakeOrderRepo
import iti.mad.marketly.data.repository.order.OrderRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderViewmodelTest {

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

    lateinit var viewmodel: OrderViewmodel
    lateinit var orderRepo: FakeOrderRepo
    lateinit var remoteDataSource: FakeRemoteDataSource
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()



    @get:Rule
    private var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(orderList = orderModelList)
        orderRepo = FakeOrderRepo(remoteDataSource)

        viewmodel = OrderViewmodel(orderRepo)
        Dispatchers.setMain(testDispatcher)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getAllOrders_getExpectedResult_checkResponseIsEqualExpectedResult() = runTest {
        // Given
        var expectedResult = ResponseState.OnSuccess(orderModelList)
        // When
        viewmodel.getAllOrders()
        var result = viewmodel.orderResponse.value

        // Then
        assertEquals(expectedResult, result)

    }

}