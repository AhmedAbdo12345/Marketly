package iti.mad.marketly.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: IAuthRepository) : ViewModel() {

    private val _customerResponse =
        MutableStateFlow<ResponseState<CustomerBody>>(ResponseState.OnLoading())
    val customerRespoonse: StateFlow<ResponseState<CustomerBody>> = _customerResponse

    fun registerUser(customer: CustomerBody) {
        _customerResponse.value = ResponseState.OnLoading()
        viewModelScope.launch {
                repo.registerUser(customer).flowOn(Dispatchers.IO).catch { e ->
                    _customerResponse.value =
                        ResponseState.OnError(e.localizedMessage ?: "eerrror")
                    print(e.printStackTrace())
                }.collect {
                    _customerResponse.value = ResponseState.OnSuccess(it)
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