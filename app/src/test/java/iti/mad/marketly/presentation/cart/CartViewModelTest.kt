package iti.mad.marketly.presentation.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.repository.cart.FakeCartRepo
import iti.mad.marketly.data.repository.favourite_repo.FakeFavRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.presentation.favourite.FavouriteViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

import org.junit.Assert.*

class CartViewModelTest {
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var fakeCartRepo: FakeCartRepo
    lateinit var viewModel: CartViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        fakeRemoteDataSource=FakeRemoteDataSource()
        fakeCartRepo = FakeCartRepo(fakeRemoteDataSource)
        viewModel = CartViewModel(fakeCartRepo)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun saveCart() {
        lateinit var actual: ResponseState<List<CartModel>>
        val cartModel = CartModel(6,"",80,500.0,"testItem")
        val cartList = mutableListOf<CartModel>(
            CartModel(1,"",50,90.0,"item1")
            , CartModel(2,"",40,100.0,"item2"), CartModel(3,"",90,120.0,"item3"),
            CartModel(6,"",80,500.0,"testItem")
        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.saveCart(cartModel)
            actual = viewModel._cartResponse.value
            viewModel.getAllCart()
            Assert.assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._cartResponse.value
            Assert.assertEquals(ResponseState.OnSuccess(cartList), actual)
        }
    }

    @Test
    fun getAllCart() {
        lateinit var actual: ResponseState<List<CartModel>>

        val cartList = mutableListOf<CartModel>(
            CartModel(1,"",50,90.0,"item1")
            , CartModel(2,"",40,100.0,"item2"), CartModel(3,"",90,120.0,"item3")

        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.getAllCart()
            actual = viewModel._cartResponse.value
            viewModel.getAllCart()
            Assert.assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._cartResponse.value
            Assert.assertEquals(ResponseState.OnSuccess(cartList), actual)
        }
    }

    @Test
    fun deleteCartItem() {
        lateinit var actual: ResponseState<List<CartModel>>
        val cartList = mutableListOf<CartModel>(
            CartModel(1,"",50,90.0,"item1")
            , CartModel(2,"",40,100.0,"item2")

        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.deleteCartItem("3")
            actual = viewModel._cartResponse.value
            viewModel.getAllCart()
            Assert.assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._cartResponse.value
            Assert.assertEquals(ResponseState.OnSuccess(cartList), actual)
        }
    }

    @Test
    fun clearCart() {
        lateinit var actual: ResponseState<List<CartModel>>
        val cartList = mutableListOf<CartModel>(
        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.clearCart()
            actual = viewModel._cartResponse.value
            viewModel.getAllCart()
            Assert.assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._cartResponse.value
            Assert.assertEquals(ResponseState.OnSuccess(cartList), actual)
        }
    }
}