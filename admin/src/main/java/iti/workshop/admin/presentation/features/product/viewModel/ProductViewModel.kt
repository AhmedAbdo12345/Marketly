package iti.workshop.admin.presentation.features.product.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.utils.DataResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val _repo: IProductRepository
    ) : ViewModel() {

    private val _productCount = MutableStateFlow<DataResponseState<Count>>(DataResponseState.OnLoading())
    val productCount:StateFlow<DataResponseState<Count>> get() = _productCount


    fun getCountOfProducts(){
        viewModelScope.launch {
            val response = _repo.getCount()
            if (response.isSuccessful){
                _productCount.value = DataResponseState.OnSuccess(response.body() ?: Count(0))
            }else{
                _productCount.value = DataResponseState.OnError(response.errorBody().toString())
            }
        }
    }
}