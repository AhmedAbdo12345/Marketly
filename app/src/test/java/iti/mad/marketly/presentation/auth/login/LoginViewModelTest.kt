package iti.mad.marketly.presentation.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.repository.authRepository.FakeAuthRepo
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    lateinit var fakeRepo: IAuthRepository
    lateinit var viewModel: LoginViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        fakeRepo = FakeAuthRepo()
        viewModel = LoginViewModel(fakeRepo)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun loginWithEmail() {
        lateinit var actual: ResponseState<CustomerResponse>
        val expected = CustomerResponse(
            listOf(
                Customer(
                    id = 1569788,
                    first_name = "mahmoudsayed",
                    email = "mahmoudsayed@gmail.com",
                    currency = "EGP"
                )
            )
        )

        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.loginWithEmail("mahmoudsayed@gmail.com")
            actual = viewModel.customerRespoonse.value
            assertEquals(ResponseState.OnLoading<CustomerResponse>(true), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.customerRespoonse.value
            assertEquals(ResponseState.OnSuccess(expected), actual)
        }
    }
}