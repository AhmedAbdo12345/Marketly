package iti.mad.marketly.presentation.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.data.model.category.Image
import iti.mad.marketly.data.repository.category.CategoryRepo
import iti.mad.marketly.data.repository.category.FakeCategoryRepo
import iti.mad.marketly.data.repository.order.FakeOrderRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.presentation.order.OrderViewmodel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryViewModelTest {
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
    lateinit var viewmodel: CategoryViewModel
    lateinit var categoryRepo: FakeCategoryRepo
    lateinit var remoteDataSource: FakeRemoteDataSource
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()



    @get:Rule
    private var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(customList = customeCollectionList)
        categoryRepo = FakeCategoryRepo(remoteDataSource)

        viewmodel = CategoryViewModel(categoryRepo)
        Dispatchers.setMain(testDispatcher)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun getAllCategory_getExpectedResult_checkResponseIsEqualExpectedResult() = runTest {
        // Given
        var expectedResult = ResponseState.OnSuccess(CategoryResponse(customeCollectionList))
        // When
        viewmodel.getAllCategory()
        var result = viewmodel.category.value

        // Then
        assertEquals(expectedResult, result)

    }

}