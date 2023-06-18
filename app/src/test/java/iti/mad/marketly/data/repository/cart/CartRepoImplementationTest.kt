package iti.mad.marketly.data.repository.cart

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CartRepoImplementationTest {
lateinit var cartRepo: CartRepoInterface
lateinit var remoteDataSource: IRemoteDataSource
    @Before
    fun setUp() {

       remoteDataSource = FakeRemoteDataSource()
       cartRepo = FakeCartRepo(remoteDataSource)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun saveProductInOrder() {
    }

    @Test
    fun saveCartProduct() {
        val cartModel = CartModel(6,"",80,500.0,"testItem")
        cartRepo.saveCartProduct(cartModel)
        runTest {
            val actualList =cartRepo.getAllCartProducts().first()
            val actualItem = actualList.get(actualList.size-1)
            Assert.assertEquals(cartModel,actualItem)
        }
    }

    @Test
    fun getAllCartProducts() {
             val cartList = mutableListOf<CartModel>(
        CartModel(1,"",50,90.0,"item1")
        , CartModel(2,"",40,100.0,"item2"), CartModel(3,"",90,120.0,"item3")
    )
        runTest {
            val actualList = cartRepo.getAllCartProducts().first()
            Assert.assertEquals(cartList,actualList)
        }
    }

    @Test
    fun deleteCartItem() {
        val cartList = mutableListOf<CartModel>(
            CartModel(1,"",50,90.0,"item1")
            , CartModel(2,"",40,100.0,"item2")

        )
        runTest {
            cartRepo.deleteCartItem("3")
            val actualList = cartRepo.getAllCartProducts().first()
            Assert.assertEquals(cartList,actualList)
        }
    }
    @Test
    fun clearCartItems(){
        val size = 0

        runTest{
            cartRepo.clearCart()
            val actualSize = cartRepo.getAllCartProducts().first().size
            Assert.assertEquals(size,actualSize)
        }
    }
}