package iti.mad.marketly.data.source.remote.retrofit

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ApiServiceTest {
        var api : ApiService? = null
    @Before
    fun setUp() {
        api = RetrofitInstance.api
    }

    @After
    fun tearDown() {
        api = null
    }

    @Test
    fun getBrandsFromAPI_checkResponseIsNotNull() = runBlocking{
        // Given
        // When
        var result = api!!.getBrandsFromAPI()
        // Then
         assertThat(result.smart_collections,IsNull.notNullValue())
    }

    @Test
    fun getCategoryFromAPI_checkResponseIsNotNull() = runBlocking{
        // Given
        // When
        var result = api!!.getCategoryFromAPI()
        // Then
        assertThat(result.custom_collections,IsNull.notNullValue())
    }

    @Test
    fun getProductFromApi_checkResponseIsNotNull() = runBlocking{
        // Given
        // When
        var result = api!!.getProductFromApi("449167294774")
        // Then
        assertThat(result.products,IsNull.notNullValue())
    }
}