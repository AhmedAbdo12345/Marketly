package iti.mad.marketly.data.repository.settings

import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.ConversionRates
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.repository.cart.CartRepoInterface
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class SettingsRepoImplementationTest {
    lateinit var settingsRepo: SettingsRepoInterface
    lateinit var remoteDataSource: IRemoteDataSource
    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource()
        settingsRepo = FakeSettingsRepo(remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getExchangeRate() {
        val currencyResponse = CurrencyResponse("EGP", ConversionRates(30.5,1),"",""
            ,"",0,"",0,"")
        runTest {
            val actual = settingsRepo.getExchangeRate().first()
            Assert.assertEquals(currencyResponse,actual)
        }
    }

    @Test
    fun saveAddress() {
        val addressSaved = Address("6","testCountry","testCity","testStreet")
        settingsRepo.saveAddress(addressSaved)
        runTest {
            val actualList =settingsRepo.getAllAddresses().first()
            val actualItem = actualList.get(actualList.size-1)
            Assert.assertEquals(addressSaved,actualItem)
        }
    }

    @Test
    fun getAllAddresses() {
        val addressList = mutableListOf<Address>(
            Address("1","Egypt","Suez","EL salam2"),
            Address("2","Egypt","EL Sharqia","EL Zaqazeq")
        )
        runTest {
           val actualList=settingsRepo.getAllAddresses().first()
           Assert.assertEquals(addressList,actualList)
        }
    }

    @Test
    fun deleteAddress() {
        val addressList = mutableListOf<Address>(

            Address("2","Egypt","EL Sharqia","EL Zaqazeq")
        )
        runTest {
            settingsRepo.deleteAddress("1")
            val actualList = settingsRepo.getAllAddresses().first()
            Assert.assertEquals(addressList,actualList)
        }
    }
}