package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BrandsRepoImplTest {

    val smartCollectionList = listOf<SmartCollection>(
        SmartCollection(
            admin_graphql_api_id = "",
            body_html = "",
            disjunctive = true,
            handle = "adidas",
            id = 449167294774,
            image = null,
            published_at = "2023-05-31T05:57:05-04:00",
            published_scope = "",
            rules = null,
            sort_order = "best-selling",
            template_suffix = "",
            title = "ADIDAS",
            updated_at = "2023-06-05T12:52:34-04:00"
        )
    )
    lateinit var remoteDataSource: FakeRemoteDataSource
    lateinit var brandsRepo: BrandsRepoImpl

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(smartList = smartCollectionList)
        brandsRepo = BrandsRepoImpl(remoteDataSource)
    }



    @Test
    fun getBrands_noInput_checkResponseInNotNull() = runBlocking {
        // Given
        // When
       var result = brandsRepo.getBrands()
        // Then
        result.collect{
            assertThat(result, IsNull.notNullValue())
        }
    }


}