package iti.mad.marketly.presentation.categoryProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.categoryProduct.CategoryProductResponse
import iti.mad.marketly.data.repository.categoryProduct.CategoryProductRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CategoryProductViewModel (val categoryProductRepo: CategoryProductRepo): ViewModel() {
    private val categoryProduct: MutableStateFlow<ResponseState<CategoryProductResponse>> = MutableStateFlow(
        ResponseState.OnLoading())
    val _categoryProduct : StateFlow<ResponseState<CategoryProductResponse>> = categoryProduct

    fun getAllCategoryProduct( collectionID:Long){
        viewModelScope.launch {
            categoryProductRepo.getCategoryProduct(collectionID).flowOn(Dispatchers.IO).catch {
                categoryProduct.emit(ResponseState.OnError(it.localizedMessage))

            }.collect{
                categoryProduct.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CategoryProductViewModel(AppDependencies.categoryProductRepo)
            }
        }
    }
}
