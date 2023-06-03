package iti.workshop.admin.data.remote.retrofit.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.dto.AddImage
import iti.workshop.admin.data.dto.PostImage
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.data.dto.VariantSingleResponseAndRequest
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductVariantCallApiTest {
    private lateinit var productVariantCallApi: ProductVariantCallApi

    private val product_id = 8400380395830
    private val variant_id = 45418408837430

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        productVariantCallApi = RetrofitInstance.productVariantCallApi
    }

    @Test
    fun getCountProductVariant() = runBlocking {
        val response = async { productVariantCallApi.getCountProductVariant(product_id) }
        print("count is: ${response.await().body()?.count}")
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }

    @Test
    fun getProductVariants() = runBlocking {
        // When
        val response = async { productVariantCallApi.getProductVariants(product_id) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())

    }

    @Test
    fun getProductVariant() = runBlocking {
        // When
        val response =
            async { productVariantCallApi.getProductVariant(8400380395830, 45418385735990) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }

    @Test
    fun addProductVariant() = runBlocking {
        // Given
        val postVariant = VariantSingleResponseAndRequest(
            variant = Variant(
                option1 = "YELLOWs", price = "1.0"
            )
        )
        // When
        val response = async { productVariantCallApi.addProductVariant(product_id, postVariant) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("201"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())

    }

    @Test
    fun updateProductVariant() = runBlocking {
        // Given
        val postVariant = VariantSingleResponseAndRequest(
            variant = Variant(
                option1 = "YELLOhhhhWs", price = "1.0"
            )
        )
        // When
        val response = async {
            productVariantCallApi.updateProductVariant(
                8400380395830,
                45418385735990,
                postVariant
            )
        }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())

    }

    @Test
    fun deleteProductVariant() = runBlocking {
        // When
        val response = async { productVariantCallApi.deleteProductVariant(8400380395830, 45418510778678) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))

    }
}
