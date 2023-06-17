package iti.mad.marketly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.repository.settings.SettingsRepoInterface
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: SettingsRepoInterface) : ViewModel() {
    private var currency: MutableStateFlow<ResponseState<CurrencyResponse>> =
        MutableStateFlow(ResponseState.OnLoading(false))
    val _currency: StateFlow<ResponseState<CurrencyResponse>> = currency
    private var addressResponse: MutableStateFlow<ResponseState<List<iti.mad.marketly.data.model.settings.Address>>> =
        MutableStateFlow(ResponseState.OnLoading(false))
    val _addressResponse: StateFlow<ResponseState<List<iti.mad.marketly.data.model.settings.Address>>> =
        addressResponse

    fun getExchangeRate() {
        viewModelScope.launch {
            repo.getExchangeRate().flowOn(Dispatchers.IO).catch {
                currency.emit(ResponseState.OnError(it.localizedMessage))
            }.collect {
                currency.emit(ResponseState.OnSuccess(it))
            }
        }
    }

    fun setAddresses(addresses: iti.mad.marketly.data.model.settings.Address) {
        repo.saveAddress(addresses)
    }

    fun getAddresses() {
        viewModelScope.launch {
            repo.getAllAddresses().flowOn(Dispatchers.IO).catch {
                addressResponse.emit(ResponseState.OnError(it.localizedMessage))
            }.collect {
                addressResponse.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    fun saveAddress(addresses: iti.mad.marketly.data.model.settings.Address){
        repo.saveAddress(addresses)
    }
    fun deleteAddress(addresses: iti.mad.marketly.data.model.settings.Address){
        repo.deleteAddress(addresses.AddressID)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(AppDependencies.settingsRepo)
            }
        }
    }
}