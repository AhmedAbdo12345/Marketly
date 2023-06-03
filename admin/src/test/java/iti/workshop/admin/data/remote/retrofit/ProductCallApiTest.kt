package iti.workshop.admin.data.remote.retrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.robolectric.annotation.Config
import retrofit2.Retrofit

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
}