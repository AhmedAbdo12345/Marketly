package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.ResultResponse
import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.presentation.view.states.BrandApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandsViewModel: ViewModel() {
   private var brands : MutableStateFlow<ResultResponse<BrandsResponse>> = MutableStateFlow(ResultResponse.OnLoading())
    val _brands: StateFlow<ResultResponse<BrandsResponse>> = brands



    fun getAllBrands( brandsRepo: BrandsRepo){
        viewModelScope.launch{


                brandsRepo.getBrands().flowOn(Dispatchers.IO).catch {
                    brands.emit(ResultResponse.OnError(it.localizedMessage))

                }.collect{
                    brands.emit(ResultResponse.OnSuccess(it))
                }


        }
    }
}