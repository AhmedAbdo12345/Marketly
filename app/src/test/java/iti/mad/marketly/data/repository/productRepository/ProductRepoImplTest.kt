package iti.mad.marketly.data.repository.productRepository

import android.util.Log
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.math.log

@ExperimentalCoroutinesApi
class ProductRepoImplTest {

    private var productResponse = mutableMapOf(
        "1" to mutableListOf(
            Product(
                id = 831593473078,
                title = "MEN'S SHOES SMITH",
                body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "SMITH",
                isFavourite = true

            ), Product(
                id = 8392651580,
                title = "ADIDAS | KID'S STAN SMITH",
                body_html = "The Stan Smith owned the tennis court in the '70s. Today it runs the streets with the same clean, classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
                vendor = "ADIDAS",
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
                id = 4151515515,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE",
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

    lateinit var remoteDataSource: FakeRemoteDataSource
    lateinit var productRepo: ProductRepo
    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(productRespnse = productResponse)
        productRepo = ProductRepoImpl(remoteDataSource)
    }

    @Test
    fun getProducts_NoInput_checkProductIDforForFirstProductInCollectionThree() = runBlocking {
        // Given

        // When
       var result = productRepo.getProducts("3")
        // Then
        result.collect{
            assertThat(it.products[0].id, CoreMatchers.`is`(8391229473078))

        }

    }
}