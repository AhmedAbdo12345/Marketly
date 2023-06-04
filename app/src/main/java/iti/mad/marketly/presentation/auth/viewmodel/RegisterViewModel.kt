package iti.mad.marketly.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.ResultResponse
import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: IAuthRepository) : ViewModel() {

    private val _customerResponse =
        MutableStateFlow<ResultResponse<CustomerBody>>(ResultResponse.OnLoading(true))
    val customerRespoonse: StateFlow<ResultResponse<CustomerBody>> = _customerResponse

    fun registerUser(customer: CustomerBody) {
        _customerResponse.value = ResultResponse.OnLoading(true)
        viewModelScope.launch {
            val newResponseCustomer =
                repo.registerUser(customer).flowOn(Dispatchers.IO).catch { e ->
                    _customerResponse.value =
                        ResultResponse.OnError(e.localizedMessage ?: "eerrror")
                    print(e.printStackTrace())
                }.collect {
                    _customerResponse.value = ResultResponse.OnSuccess(it)
                    print(it.toString())
                }

        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
               RegisterViewModel(AppDependencies.authRepository)
            }
        }
    }

}