package iti.mad.marketly.data.repository.settings

import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import kotlinx.coroutines.flow.Flow

interface SettingsRepoInterface {
    suspend fun getExchangeRate(): Flow<CurrencyResponse>
    fun saveAddress(address: Address)
    suspend fun getAllAddresses():Flow<List<Address>>
}