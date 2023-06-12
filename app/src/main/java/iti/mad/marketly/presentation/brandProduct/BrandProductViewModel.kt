package iti.mad.marketly.presentation.brandProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.product.ProductResponse
import iti.mad.marketly.data.repository.productRepository.ProductRepo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BrandProductViewModel : ViewModel() {
    private var brandProduct : MutableStateFlow<ResponseState<ProductResponse>> = MutableStateFlow(
        ResponseState.OnLoading())
    val _brandProduct: StateFlow<ResponseState<ProductResponse>> = brandProduct



    fun getAllBrandProduct( brandsRepo: ProductRepo,id:String){
        viewModelScope.launch{


            brandsRepo.getProducts(id).flowOn(Dispatchers.IO).catch {
                brandProduct.value= ResponseState.OnError(it.localizedMessage?:"")

            }.collect{
                brandProduct.value = ResponseState.OnSuccess(it)
            }


        }
    }
}