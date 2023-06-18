package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.data.model.category.Image
import iti.mad.marketly.data.repository.brands.BrandsRepoImpl
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryRepoImplTest {

    val customeCollectionList = listOf<CustomCollection>(
        CustomCollection(
            admin_graphql_api_id = "",
            body_html = "",
            handle = "men",
            id = 449166377270,
            image = Image(
                "",
                "2023-05-31T05:57:17-04:00",
                200,
                "https://cdn.shopify.com/s/files/1/0775/1142/6358/collections/cde37406b76337f4438c62f57be75df2.jpg?v=1685527037",
                480
            ),
            published_at = "2023-05-31T05:38:28-04:00",
            published_scope = "global",
            sort_order = "best-selling",
            template_suffix = "null",
            title = "Home page",
            updated_at = ""
        )
    )
    lateinit var remoteDataSource: FakeRemoteDataSource
    lateinit var categoryRepo: CategoryRepo

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(customList = customeCollectionList)
        categoryRepo = CategoryRepoImpl(remoteDataSource)
    }


    @Test
    fun getCategory_noInput_checkResponseIsNotNull() = runBlocking {
        // Given
        // When
       var result = remoteDataSource.getCategory()
        // Then
        result.collect{
            assertThat(result, IsNull.notNullValue())
        }

    }
}