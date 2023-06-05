package iti.mad.marketly.presentation.brandProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.repository.brandproduct.BrandProductRepo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandProductViewModel : ViewModel() {
    private var brandProduct : MutableStateFlow<ResponseState<BrandProductResponse>> = MutableStateFlow(
        ResponseState.OnLoading())
    val _brandProduct: StateFlow<ResponseState<BrandProductResponse>> = brandProduct



    fun getAllBrandProduct( brandsRepo: BrandProductRepo){
        viewModelScope.launch{


            brandsRepo.getBrandProduct().flowOn(Dispatchers.IO).catch {
                brandProduct.emit(ResponseState.OnError(it.localizedMessage))

            }.collect{
                brandProduct.emit(ResponseState.OnSuccess(it))
            }


        }
    }
}