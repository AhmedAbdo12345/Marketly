package iti.workshop.admin.data.remote.retrofit.coupon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DiscountCodeCallApiTest {
    private lateinit var discountCodeCallApi: DiscountCodeCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        discountCodeCallApi = RetrofitInstance.discountCodeCallApi
    }

    @Test
    fun getCount() = runBlocking{
        val response = async { discountCodeCallApi.getCount() }
        print("count is: ${response.await().body()?.count}")
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }
}