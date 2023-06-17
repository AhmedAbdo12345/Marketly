package iti.mad.marketly.data.repository.favourite_repo


import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouriteRepTest {
    lateinit var repository: IFavouriteRepo
    lateinit var remoteDataSource: IRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource()
        repository = FavouriteRep(remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addProductToFavourite_newuser_addedSuccessfully() {
        val product = Product(
            id = 9849, title = "ITI", body_html = "abu treika", vendor = "NIKE"
        )
        val products = listOf<Product>(
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

            ), Product(
                id = 8391229473081,
                title = "ADIDAS | AL AHLY T-shirt",
                body_html = "The t shirt of mohamed abu treika",
                vendor = "NIKE"
            )
        )

        runTest {
            repository.addProductToFavourite("4", product).first()
            val actual = repository.getAllFavourite("4").first().products
            Assert.assertEquals(listOf(product), actual)
        }


    }

    @Test
    fun isFavourite() {
    }

    @Test
    fun getAllFavourite() {
        val products = mutableListOf<Product>(
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
            val actual = repository.getAllFavourite("3").first().products
            Assert.assertEquals(products, actual)
        }
    }

    @Test
    fun getAllFavouriteIDS() {
        val ids = listOf<String>("8391229473078", "8391229473079")
        runTest {
            val actual = repository.getAllFavouriteIDS("3").first()
            Assert.assertEquals(ids, actual)
        }
    }

    @Test
    fun deleteFromFavourite() {
        //TODO
    }
}