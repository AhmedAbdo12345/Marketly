package iti.mad.marketly.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.repository.category.CategoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CategoryViewModel(val categoryRepo: CategoryRepo): ViewModel() {
    private val category: MutableStateFlow<ResponseState<CategoryResponse>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val _category : StateFlow<ResponseState<CategoryResponse>> = category

    fun getAllCategory(){
        viewModelScope.launch {
            categoryRepo.getCategory().flowOn(Dispatchers.IO).catch {
                category.emit(ResponseState.OnError(it.localizedMessage))

            }.collect{
                category.emit(ResponseState.OnSuccess(it))

            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CategoryViewModel(AppDependencies.categoryRepo)
            }
        }
    }
}
