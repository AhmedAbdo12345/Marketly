package iti.mad.marketly.presentation.home.brands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.repository.brands.BrandsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandsViewModel: ViewModel() {
   private var brands : MutableStateFlow<ResponseState<BrandsResponse>> = MutableStateFlow(
       ResponseState.OnLoading())
    val _brands: StateFlow<ResponseState<BrandsResponse>> = brands

    fun getAllBrands( brandsRepo: BrandsRepo){
        viewModelScope.launch{


                brandsRepo.getBrands().flowOn(Dispatchers.IO).catch {
                    brands.emit(ResponseState.OnError(it.localizedMessage))

                }.collect{
                    brands.emit(ResponseState.OnSuccess(it))
                }


        }
    }
}