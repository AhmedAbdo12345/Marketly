package iti.mad.marketly.presentation.productdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class ProductDetailsViewModel(private val repo: ProductDetailsRepository) : ViewModel() {

    private val _productDetails =
        MutableStateFlow<ResponseState<ProductDetails>>(ResponseState.OnLoading())
    val productDetails: StateFlow<ResponseState<ProductDetails>> = _productDetails

    fun getProductDetails(id: Long) {
        _productDetails.value = ResponseState.OnLoading()
        viewModelScope.launch {
            repo.getProductDetails(id).flowOn(Dispatchers.IO).catch { e ->
                _productDetails.value = ResponseState.OnError(e.localizedMessage ?: "eerrror")
                print(e.printStackTrace())
            }.collect {
                _productDetails.value = ResponseState.OnSuccess(it)
                print(it.toString())
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProductDetailsViewModel(AppDependencies.productDetailsRepository)
            }
        }
    }

}