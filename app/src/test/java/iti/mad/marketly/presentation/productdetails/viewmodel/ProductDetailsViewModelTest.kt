package iti.mad.marketly.presentation.productdetails.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.repository.productdetailsRepo.FakeFavRepo
import iti.mad.marketly.data.repository.productdetailsRepo.FakeProductDetailsRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class ProductDetailsViewModelTest {
    lateinit var repository: FakeProductDetailsRepo
    lateinit var fakeFavRepo: FakeFavRepo
    lateinit var viewModel: ProductDetailsViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = FakeProductDetailsRepo()
        fakeFavRepo = FakeFavRepo()
        viewModel = ProductDetailsViewModel(repository, fakeFavRepo)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getProductDetails() {
        lateinit var actual: ResponseState<ProductDetails>
        val expectedProductDetails = ProductDetails(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH"
            )
        )
        runTest {
            viewModel.getProductDetails(8391229473078)
            actual = viewModel.productDetails.first()
        }
        Assert.assertEquals(actual, ResponseState.OnSuccess(expectedProductDetails))
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
            viewModel.isFavourite("1", product)
            actual = viewModel.isFavourite.first()
        }
        Assert.assertEquals(ResponseState.OnSuccess(true), actual)

    }


    @Test
    fun addProductToFavourite_product_addedSuccessfully() {
        lateinit var actual: ResponseState<String>
        val product = Product(
            id = 7897978979,
            title = "zamalek shirt",
            body_html = "heel and tongue logos and lightweight step-in cushioning.",
            vendor = "zamalek"
        )

        runTest {
            viewModel.addProductToFavourite("3", product)
            actual = viewModel.addedSuccessfully.first()
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
            viewModel.deleteProductFromFavourite("3", product)
            actual = viewModel.deletedSuccessfully.first()
            Assert.assertEquals(ResponseState.OnSuccess("deleted Successfully"), actual)
        }

    }

}