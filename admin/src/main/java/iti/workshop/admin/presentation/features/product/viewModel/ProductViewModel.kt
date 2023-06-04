package iti.workshop.admin.presentation.features.product.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.features.product.models.ProductUIModel
import iti.workshop.admin.presentation.utils.DataResponseState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val _repo: IProductRepository
    ) : ViewModel() {

    private val _productResponses = MutableStateFlow<DataResponseState<ProductUIModel>>(DataResponseState.OnLoading())
    val productResponses:StateFlow<DataResponseState<ProductUIModel>> get() = _productResponses


    fun getCountOfProducts(){
        viewModelScope.launch {
            val responseCount = async{ _repo.getCount() }
            val responseProductList = async { _repo.getProduct() }

            if (responseCount.await().isSuccessful && responseProductList.await().isSuccessful){
                val productViewModel = ProductUIModel(
                    count = responseCount.await().body(),
                    products = responseProductList.await().body()?.products
                )
                _productResponses.value = DataResponseState.OnSuccess(productViewModel)
            }
            if (!responseCount.await().isSuccessful)
                _productResponses.value = DataResponseState.OnError(responseCount.await().errorBody().toString())

            if (!responseProductList.await().isSuccessful)
                _productResponses.value = DataResponseState.OnError(responseProductList.await().errorBody().toString())


        }
    }
}