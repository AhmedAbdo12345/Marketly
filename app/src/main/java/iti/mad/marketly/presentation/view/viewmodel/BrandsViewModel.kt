package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.presentation.view.BrandApiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrandsViewModel: ViewModel() {
   private var brands : MutableStateFlow<BrandApiStatus> = MutableStateFlow(BrandApiStatus.Loading())
    val _brands: StateFlow<BrandApiStatus> = brands



    fun getAllBrands( brandsRepo: BrandsRepo){
        viewModelScope.launch{
            try {
                brands.emit(BrandApiStatus.Success(brandsRepo.getBrands()))
            }catch (e:Exception) {
                brands.emit(BrandApiStatus.Failed(e.localizedMessage))
            }

        }
    }
}