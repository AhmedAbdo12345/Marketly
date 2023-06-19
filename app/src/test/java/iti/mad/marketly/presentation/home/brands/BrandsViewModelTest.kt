package iti.mad.marketly.presentation.home.brands

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.data.repository.brands.FakeBrandsRepo
import iti.mad.marketly.data.repository.category.FakeCategoryRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.presentation.category.CategoryViewModel
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

class BrandsViewModelTest {
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
    lateinit var viewmodel: BrandsViewModel
    lateinit var brandsRepo: FakeBrandsRepo
    lateinit var remoteDataSource: FakeRemoteDataSource
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()



    @get:Rule
    private var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource(smartList = smartCollectionList)
        brandsRepo = FakeBrandsRepo(remoteDataSource)

        viewmodel = BrandsViewModel(brandsRepo)
        Dispatchers.setMain(testDispatcher)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun getAllBrands_getExpectedResult_checkResponseIsEqualExpectedResult() = runTest {
        // Given
        var expectedResult = ResponseState.OnSuccess(BrandsResponse(smartCollectionList))
        // When
        viewmodel.getAllBrands()
        var result = viewmodel.brands.value

        // Then
        assertEquals(expectedResult, result)

    }
}