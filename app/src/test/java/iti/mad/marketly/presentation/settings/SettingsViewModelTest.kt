package iti.mad.marketly.presentation.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.repository.cart.FakeCartRepo
import iti.mad.marketly.data.repository.settings.FakeSettingsRepo
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.presentation.cart.CartViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class SettingsViewModelTest {
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var fakeSettingsRepo: FakeSettingsRepo
    lateinit var viewModel: SettingsViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        fakeRemoteDataSource=FakeRemoteDataSource()
        fakeSettingsRepo = FakeSettingsRepo(fakeRemoteDataSource)
        viewModel = SettingsViewModel(fakeSettingsRepo)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun setAddresses() {
        lateinit var actual: ResponseState<List<iti.mad.marketly.data.model.settings.Address>>
        val addressSaved = Address("6","testCountry","testCity","testStreet")
        val addressList = mutableListOf<Address>(
            Address("1","Egypt","Suez","EL salam2"),
            Address("2","Egypt","EL Sharqia","EL Zaqazeq"),
            Address("6","testCountry","testCity","testStreet")
        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.setAddresses(addressSaved)
            actual = viewModel._addressResponse.value
            viewModel.getAddresses()
            assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._addressResponse.value
            assertEquals(ResponseState.OnSuccess(addressList), actual)
        }
    }

    @Test
    fun getAddresses() {
        lateinit var actual: ResponseState<List<iti.mad.marketly.data.model.settings.Address>>

        val addressList = mutableListOf<Address>(
            Address("1","Egypt","Suez","EL salam2"),
            Address("2","Egypt","EL Sharqia","EL Zaqazeq")

        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.getAddresses()
            actual = viewModel._addressResponse.value
            viewModel.getAddresses()
            assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._addressResponse.value
            assertEquals(ResponseState.OnSuccess(addressList), actual)
        }
    }



    @Test
    fun deleteAddress() {
        lateinit var actual: ResponseState<List<iti.mad.marketly.data.model.settings.Address>>
val deletedAddress = Address("1","Egypt","Suez","EL salam2")
        val addressList = mutableListOf<Address>(

            Address("2","Egypt","EL Sharqia","EL Zaqazeq")

        )
        runTest {
            testDispatcher.pauseDispatcher()
            viewModel.deleteAddress(deletedAddress)
            actual = viewModel._addressResponse.value
            viewModel.getAddresses()
            assertEquals(ResponseState.OnLoading<List<CartModel>>(false), actual)
            testDispatcher.resumeDispatcher()
            actual = viewModel._addressResponse.value
            assertEquals(ResponseState.OnSuccess(addressList), actual)
        }
    }
}