package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.presentation.view.BrandApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class BrandsViewModel: ViewModel() {
   private var brands : MutableStateFlow<BrandApiStatus> = MutableStateFlow(BrandApiStatus.Loading())
    val _brands: StateFlow<BrandApiStatus> = brands



    fun getAllBrands( brandsRepo: BrandsRepo){
        viewModelScope.launch{


                brandsRepo.getBrands().flowOn(Dispatchers.IO).catch {
                    brands.emit(BrandApiStatus.Failed(it.localizedMessage))

                }.collect{
                    brands.emit(BrandApiStatus.Success(it))
                }


        }
    }
}