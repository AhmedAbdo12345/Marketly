package iti.mad.marketly.data.repository.settings

import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeSettingsRepo(val remote:IRemoteDataSource):SettingsRepoInterface {
    override suspend fun getExchangeRate(): Flow<CurrencyResponse> = remote.getExchangeRate()

    override fun saveAddress(address: Address) {
        remote.saveAddress(address)
    }

    override suspend fun getAllAddresses(): Flow<List<Address>> = remote.getAllAddresses()

    override fun deleteAddress(addressID: String) {
       remote.deleteAddress(addressID)
    }
}