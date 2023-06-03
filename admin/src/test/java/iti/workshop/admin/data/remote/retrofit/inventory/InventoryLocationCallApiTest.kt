package iti.workshop.admin.data.remote.retrofit.inventory

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
class InventoryLocationCallApiTest{
    private lateinit var inventoryLocationCallApi: InventoryLocationCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        inventoryLocationCallApi = RetrofitInstance.inventoryLocationCallApi
    }

    @Test
    fun getCount() = runBlocking{
        val response = async { inventoryLocationCallApi.retrieveCountLocations() }
        print("count is: ${response.await().body()?.count}")
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
    }

    @Test
    fun retrieveCountLocations() = runBlocking {
        // When
        val response = async{ inventoryLocationCallApi.retrieveCountLocations() }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }

    @Test
    fun retrieveSingleLocationByID() = runBlocking {
        // When
        val response = async{ inventoryLocationCallApi.retrieveSingleLocationByID(84887011638) }
        // Then
        MatcherAssert.assertThat(response.await().code().toString(), Is.`is`("200"))
        MatcherAssert.assertThat(response.await().body(), CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.await().errorBody(), CoreMatchers.nullValue())
    }
}