package iti.mad.marketly.presentation.categoryProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.repository.productRepository.ProductRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CategoryProductViewModel (val categoryProductRepo: ProductRepo): ViewModel() {
    private val categoryProduct: MutableStateFlow<ResponseState<ProductResponse>> = MutableStateFlow(
        ResponseState.OnLoading())
    val _categoryProduct : StateFlow<ResponseState<ProductResponse>> = categoryProduct

    fun getAllCategoryProduct( collectionID:String){
        viewModelScope.launch {
            categoryProductRepo.getProducts(collectionID).flowOn(Dispatchers.IO).catch {
                categoryProduct.emit(ResponseState.OnError(it.localizedMessage))

            }.collect{
                categoryProduct.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CategoryProductViewModel(AppDependencies.productRepo)
            }
        }
    }
}
