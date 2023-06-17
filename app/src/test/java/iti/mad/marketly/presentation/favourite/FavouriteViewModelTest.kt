package iti.mad.marketly.presentation.favourite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.favourite_repo.FakeFavRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    lateinit var fakeFavRepo: FakeFavRepo
    lateinit var viewModel: FavouriteViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        fakeFavRepo = FakeFavRepo()
        viewModel = FavouriteViewModel(fakeFavRepo)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun addProductToFavourite() {
        lateinit var actual: ResponseState<String>
        val product = Product(
            id = 7897978979,
            title = "zamalek shirt",
            body_html = "heel and tongue logos and lightweight step-in cushioning.",
            vendor = "zamalek"
        )

        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.addProductToFavourite("3", product)
            actual = viewModel.addedSuccessfully.value
            Assert.assertEquals(ResponseState.OnLoading<String>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.addedSuccessfully.value
            Assert.assertEquals(ResponseState.OnSuccess("added Successfully"), actual)
        }
    }

    @Test
    fun deleteProductFromFavourite() {
        lateinit var actual: ResponseState<String>
        val product = Product(
            id = 8391229473078,
            title = "MEN'S SHOES SMITH",
            body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "SMITH",
            isFavourite = true

        )

        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.deleteProductFromFavourite("3", product)
            actual = viewModel.deletedSuccessfully.value
            Assert.assertEquals(ResponseState.OnLoading<String>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.deletedSuccessfully.value
            Assert.assertEquals(ResponseState.OnSuccess("deleted Successfully"), actual)
        }
    }

    @Test
    fun isFavourite() {
        lateinit var actual: ResponseState<Boolean>
        val product = Product(
            id = 8391229473078,
            title = "MEN'S SHOES SMITH",
            body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "SMITH"
        )

        runTest {

            testDispatcher.pauseDispatcher()
            viewModel.isFavourite("1", product)
            actual = viewModel.isFavourite.value
            Assert.assertEquals(ResponseState.OnLoading<Boolean>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.isFavourite.value
            Assert.assertEquals(ResponseState.OnSuccess(true), actual)
        }

    }

    @Test
    fun getAllFavourite() {
        lateinit var actual: ResponseState<List<Product>>
        val expectedProduct = mutableListOf<Product>(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 8391229473079,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.getAllFavourite("3")
            actual = viewModel.allFavourites.value
            Assert.assertEquals(ResponseState.OnLoading<List<Product>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.allFavourites.value
            Assert.assertEquals(ResponseState.OnSuccess<List<Product>>(expectedProduct), actual)


        }
    }
}