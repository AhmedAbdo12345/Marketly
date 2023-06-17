package iti.mad.marketly.presentation.brandProduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.favourite_repo.FakeFavRepo
import iti.mad.marketly.data.repository.order.FakeOrderRepo
import iti.mad.marketly.data.repository.productRepository.FakeProductRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.presentation.order.OrderViewmodel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class BrandProductViewModelTest {

    private var productResponse = mutableMapOf(
        "1" to mutableListOf(
            Product(
                id = 831593473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 15541511515123,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE",
                isFavourite = true
            ), Product(
                id = 54151515515,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        ),
        "2" to mutableListOf(
            Product(
                id = 156355215213,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true
            ), Product(
                id = 4552445215,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        ),
        "3" to mutableListOf(
            Product(
                id = 8391229473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 812135535152,
                title = "11 salah liverpool",
                body_html = "Mohamed salah teshirt",
                vendor = "LiverPOol",
                isFavourite = true

            )
        )
    )


    lateinit var viewmodel: BrandProductViewModel
    lateinit var productRepo: FakeProductRepo
    lateinit var remoteDataSource: FakeRemoteDataSource
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    private var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(productRespnse = productResponse)
        productRepo = FakeProductRepo(remoteDataSource)
       var favRepo = FakeFavRepo()
        viewmodel = BrandProductViewModel(productRepo,favRepo)
        Dispatchers.setMain(testDispatcher)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun getAllBrandProduct_NoInput_checkResponseIsEqualExpectedResul() = runTest {
        // Given
        var expectedResult :ResponseState.OnSuccess<List<Product>?> = ResponseState.OnSuccess(productResponse["1"])
        // When
        testDispatcher.pauseDispatcher()
        viewmodel.getAllBrandProduct("1", "123")
        var result : ResponseState<List<Product>> = (viewmodel.brandProduct.value)
        assertEquals(ResponseState.OnLoading<List<Product>>(true), result)

    }
}