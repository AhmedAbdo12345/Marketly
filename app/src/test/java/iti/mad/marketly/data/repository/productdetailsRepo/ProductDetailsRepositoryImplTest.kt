package iti.mad.marketly.data.repository.productdetailsRepo

import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductDetailsRepositoryImplTest {
    lateinit var repository: ProductDetailsRepository
    lateinit var remoteDataSource: IRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource()
        repository = ProductDetailsRepositoryImpl(remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getProductDetails_realID_return_ProductDetails() {
        lateinit var actual: ProductDetails
        runBlocking {
            repository.getProductDetails(8391229473081).collect {
                actual = it
            }
        }
        val expected = ProductDetails(
            Product(
                id = 8391229473081,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE"
            )
        )

        assertEquals(expected, actual)

    }

    @Test
    fun getProductDetails_fakeID_return_null() {
        lateinit var actual: ProductDetails
        runBlocking {
            repository.getProductDetails(8391229477081).collect {
                actual = it
            }
        }
        val expected = ProductDetails(
            null
        )

        assertEquals(expected, actual)

    }
}