package iti.workshop.admin.data.remote.retrofit.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.dto.*
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
class ProductCallApiTest {

    private lateinit var productCallApi: ProductCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        productCallApi = RetrofitInstance.productCallApi
    }

    @Test
    fun getProduct_callGetProduct_retrieveData200() = runBlocking {
        // When
        val response = async{ productCallApi.getProduct() }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }

    @Test
    fun addProduct_callAddProduct_retrieveProductData201() = runBlocking {
        // Given
        val product = PostProduct(
            Product(
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
    fun updateProduct_callUpdateProduct_retrieveProductData200() = runBlocking {
        // Given
        val product = UpdateProduct(
            Product(
                id = 8400380395830,
                body_html="<strong>Good snowboard!</strong>",
                product_type="Snowboard",
                status="draft",
                title="Mohamed Product",
                vendor="Burton",
             )
        )
        // When
        val response = async{ productCallApi.updateProduct(product.product.id ?: -1,product) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
        MatcherAssert.assertThat(response.await().body(),CoreMatchers.isA(Product::class.java))
    }


    @Test
    fun deleteProduct_callDeleteProduct_retrieveNothing200() = runBlocking {
        // Given
        val id = 8400528277814
        // When
        val response = async{ productCallApi.deleteProduct(id) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }


    @Test
    fun getCount() = runBlocking{
        val response = async { productCallApi.getCount() }
        print("count is: ${response.await().body()?.count}")
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }
}