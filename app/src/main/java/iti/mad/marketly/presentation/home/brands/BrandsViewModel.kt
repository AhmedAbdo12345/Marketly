package iti.mad.marketly.presentation.home.brands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.presentation.category.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandsViewModel(var brandsRepo: BrandsRepo): ViewModel() {
   private var _brands : MutableStateFlow<ResponseState<BrandsResponse>> = MutableStateFlow(
       ResponseState.OnLoading(false))
    val brands: StateFlow<ResponseState<BrandsResponse>> = _brands

    fun getAllBrands( ){
        viewModelScope.launch{

                brandsRepo.getBrands().flowOn(Dispatchers.IO).catch {
                    _brands.emit(ResponseState.OnError(it.localizedMessage))

                }.collect{
                    _brands.emit(ResponseState.OnSuccess(it))
                }


        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BrandsViewModel(AppDependencies.brandsRepo)
            }
        }
    }
}