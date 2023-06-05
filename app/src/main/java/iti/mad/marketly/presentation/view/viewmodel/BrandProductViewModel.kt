package iti.mad.marketly.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.ResultResponse
import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.repository.brandproduct.BrandProductRepo

import iti.mad.marketly.presentation.view.BrandProductApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandProductViewModel : ViewModel() {
    private var brandProduct : MutableStateFlow<ResultResponse<BrandProductResponse>> = MutableStateFlow(ResultResponse.OnLoading())
    val _brandProduct: StateFlow<ResultResponse<BrandProductResponse>> = brandProduct



    fun getAllBrandProduct( brandsRepo: BrandProductRepo){
        viewModelScope.launch{


            brandsRepo.getBrandProduct().flowOn(Dispatchers.IO).catch {
                brandProduct.emit(ResultResponse.OnError(it.localizedMessage))

            }.collect{
                brandProduct.emit(ResultResponse.OnSuccess(it))
            }


        }
    }
}