package iti.mad.marketly.presentation.auth.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerBody
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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    lateinit var fakeRepo: IAuthRepository
    lateinit var viewModel: RegisterViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        fakeRepo = FakeAuthRepo()
        viewModel = RegisterViewModel(fakeRepo)
        Dispatchers.setMain(testDispatcher)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun registerUser() {
        lateinit var actual: ResponseState<CustomerBody>
        val customer = CustomerBody(
            Customer(
                id = 1569788,
                first_name = "mahmoudsayed",
                email = "mahmoudsayed@gmail.com",
                currency = "EGP"
            )
        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.registerUser(customer)
            actual = viewModel.customerRespoonse.value
            Assert.assertEquals(ResponseState.OnLoading<CustomerBody>(true), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel.customerRespoonse.value
            Assert.assertEquals(ResponseState.OnSuccess(customer), actual)
        }
    }
}