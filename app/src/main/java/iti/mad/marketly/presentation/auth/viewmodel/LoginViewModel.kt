package iti.mad.marketly.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.ResultResponse
import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.data.model.CustomerResponse
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: IAuthRepository) : ViewModel() {

    private val _customerResponse =
        MutableStateFlow<ResultResponse<CustomerResponse>>(ResultResponse.OnLoading())
    val customerRespoonse: StateFlow<ResultResponse<CustomerResponse>> = _customerResponse

    fun loginWithEmail(email: String) {
        _customerResponse.value = ResultResponse.OnLoading()
        viewModelScope.launch {

            repo.loginWithEmail(email).flowOn(Dispatchers.IO).catch { e ->
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
                LoginViewModel(AppDependencies.authRepository)
            }
        }
    }

}