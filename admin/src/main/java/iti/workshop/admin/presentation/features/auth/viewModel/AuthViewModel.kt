package iti.workshop.admin.presentation.features.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.utils.DataListResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() :ViewModel() {


    private val _usersResponse:MutableStateFlow<DataListResponseState<List<User>>> = MutableStateFlow(DataListResponseState.OnLoading())
    val usersResponse:StateFlow<DataListResponseState<List<User>>> get() = _usersResponse


    private val _actionResponse = MutableStateFlow<Pair<Boolean?,String?>>(Pair(null,null))
    val actionResponse:StateFlow<Pair<Boolean?,String?>> get() = _actionResponse



    fun getUsersList(){
        val users:MutableList<User> = mutableListOf<User>().apply {
            repeat(20){
                add(User(name = "user $it", email = "user $it@mail.com"))
            }
        }
        viewModelScope.launch {
                _usersResponse.value = DataListResponseState.OnSuccess(users)
        }
    }
}