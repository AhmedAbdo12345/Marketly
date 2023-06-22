package iti.workshop.admin.presentation.features.auth.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.remote.firestore.FireStoreManager
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"
@HiltViewModel
class AuthViewModel @Inject constructor() :ViewModel() {


    private val _usersResponse:MutableStateFlow<DataListResponseState<List<User>>> = MutableStateFlow(DataListResponseState.OnLoading())
    val usersResponse:StateFlow<DataListResponseState<List<User>>> get() = _usersResponse


    private val _actionResponse = MutableStateFlow<Pair<Boolean?,String?>>(Pair(null,null))
    val actionResponse:StateFlow<Pair<Boolean?,String?>> get() = _actionResponse

    private var auth: FirebaseAuth=  FirebaseAuth.getInstance()


    fun addUser(activity: Activity, employer:User) {
        auth.createUserWithEmailAndPassword(employer.email ?: "", employer.password ?: "")
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    FireStoreManager.saveUser(employer) {
                        _actionResponse.value = Pair(true, "Employer added successfully")
                        getUsersList()
                    }
                } else {
                    _actionResponse.value = Pair(false, "There is an error during add employer")
                }
            }
    }

    fun removeUser(employer:User) {
            FireStoreManager.deleteUser(employer,{
                _actionResponse.value = Pair(true, "Employer removed successfully")
                getUsersList()
            },{
                _actionResponse.value = Pair(true, it)
            })
    }
    fun getUsersList(){
        viewModelScope.launch {
            FireStoreManager.getUsers {
                _usersResponse.value = DataListResponseState.OnSuccess(it)
            }
        }
    }
}