package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.ResultResponse
import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.repository.category.CategoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CategoryViewModel(val categoryRepo: CategoryRepo): ViewModel() {
    private val category: MutableStateFlow<ResultResponse<CategoryResponse>> = MutableStateFlow(ResultResponse.OnLoading())
    val _category : StateFlow<ResultResponse<CategoryResponse>> = category

    fun getAllCategory(){
        viewModelScope.launch {
            categoryRepo.getCategory().flowOn(Dispatchers.IO).catch {
                category.emit(ResultResponse.OnError(it.localizedMessage))

            }.collect{
                category.emit(ResultResponse.OnSuccess(it))

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
