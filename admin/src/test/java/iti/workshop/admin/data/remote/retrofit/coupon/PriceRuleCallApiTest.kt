package iti.workshop.admin.data.remote.retrofit.coupon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.remote.retrofit.product.ProductCallApi
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
class PriceRuleCallApiTest{
    private lateinit var priceRuleCallApi: PriceRuleCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        priceRuleCallApi = RetrofitInstance.priceRuleCallApi
    }

    @Test
    fun getCount() = runBlocking{
        val response = async { priceRuleCallApi.getCountPriceRule() }
        print("count is: ${response.await().body()?.count}")
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }

    @Test
    fun getPriceRule() = runBlocking {
        // When
        val response = async{ priceRuleCallApi.getPriceRules() }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }
}