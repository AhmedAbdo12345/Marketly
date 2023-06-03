package iti.workshop.admin.data.remote.retrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.dto.*
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

//@Config(sdk = [32],manifest=Config.NONE)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductCallApiTest {

    private lateinit var productCallApi: ProductCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        productCallApi = RetrofitInstance.productCallApi
    }

    @Test
    fun getProduct_callGetProduct_retrieveData() = runBlocking {
        // When
        val response = async{ productCallApi.getProduct() }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }

    @Test
    fun addProduct_callAddProduct_retrieveProductData() = runBlocking {
        // Given
        val product = PostProduct(
            PostSingleProduct(
                body_html="<strong>Good snowboard!</strong>",
                product_type="Snowboard",
                status="draft",
                title="Mohamed Product",
                vendor="Burton",
            )
        )
        // When
        val response = async{ productCallApi.addProduct(product) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("201"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
        MatcherAssert.assertThat(response.await().body(),CoreMatchers.isA(Product::class.java))
    }

    @Test
    fun updateProduct_callUpdateProduct_retrieveProductData() = runBlocking {
        // Given
        val product = UpdateProduct(
            PutSingleProduct(
                id = 8399806759222,
                body_html="<strong>Good snowboard!</strong>",
                product_type="Snowboard",
                status="draft",
                title="Mohamed Product",
                vendor="Burton",
             )
        )
        // When
        val response = async{ productCallApi.updateProduct(product.product.id,product) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
        MatcherAssert.assertThat(response.await().body(),CoreMatchers.isA(Product::class.java))
    }
}