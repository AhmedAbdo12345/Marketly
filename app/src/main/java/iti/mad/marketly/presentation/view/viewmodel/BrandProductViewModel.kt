package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.data.repository.brandproduct.BrandProductRepo

import iti.mad.marketly.presentation.view.BrandProductApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandProductViewModel : ViewModel() {
    private var brandProduct : MutableStateFlow<BrandProductApiStatus> = MutableStateFlow(BrandProductApiStatus.Loading())
    val _brandProduct: StateFlow<BrandProductApiStatus> = brandProduct



    fun getAllBrandProduct( brandsRepo: BrandProductRepo){
        viewModelScope.launch{


            brandsRepo.getBrandProduct().flowOn(Dispatchers.IO).catch {
                brandProduct.emit(BrandProductApiStatus.Failed(it.localizedMessage))

            }.collect{
                brandProduct.emit(BrandProductApiStatus.Success(it))
            }


        }
    }
}