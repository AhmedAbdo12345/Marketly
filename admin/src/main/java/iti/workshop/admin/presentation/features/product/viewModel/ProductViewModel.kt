package iti.workshop.admin.presentation.features.product.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.PostProduct
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.UpdateProduct
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.features.product.models.ProductUIModel
import iti.workshop.admin.presentation.utils.DataResponseState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val _repo: IProductRepository
    ) : ViewModel() {

    private val _productResponses = MutableStateFlow<DataResponseState<ProductUIModel>>(DataResponseState.OnLoading())
    val productResponses:StateFlow<DataResponseState<ProductUIModel>> get() = _productResponses

    private var products:MutableList<Product>? = mutableListOf()

    private val _deleteResponse = MutableStateFlow<Boolean?>(null)
    val deleteResponse:StateFlow<Boolean?> get() = _deleteResponse

    private val _addOrEditResponses = MutableStateFlow<DataResponseState<Product>>(DataResponseState.OnLoading())
    val addOrEditResponses:StateFlow<DataResponseState<Product>> get() = _addOrEditResponses



    fun deleteProduct(product: Product){
        viewModelScope.launch {
            val response = async{ _repo.deleteProduct(product.id) }
            if(response.await().isSuccessful){

                products?.remove(product)
                _productResponses.value = DataResponseState.OnSuccess(
                    ProductUIModel(
                        products = products,
                        count = Count(count = products?.size ?: 0)
                    )
                )
            }
            _deleteResponse.value = response.await().isSuccessful
        }
    }
    fun addOrEditProduct(product: Product){
        viewModelScope.launch {
            var response: Deferred<Response<Product>>?=null
            if (product.id == -1L)
                response = async { _repo.addProduct(PostProduct(product)) }
            else
                response = async { _repo.updateProduct(product.id, UpdateProduct(product)) }

            if (response.await().isSuccessful)
                response.await().body()?.let {
                    _addOrEditResponses.value = DataResponseState.OnSuccess(it)
                }
            else
                _addOrEditResponses.value = DataResponseState.OnError(response.await().errorBody().toString())
        }
    }
    fun getCountOfProducts(){
        viewModelScope.launch {

            val responseCount = async{ _repo.getCount() }
            val responseProductList = async { _repo.getProduct() }

            if (responseCount.await().isSuccessful && responseProductList.await().isSuccessful){

                val count = responseCount.await().body()
                products = responseProductList.await().body()?.products as MutableList<Product>

                if (count?.count == 0){
                    _productResponses.value = DataResponseState.OnNothingData()

                }else{
                    ProductUIModel(
                        count = count,
                        products = products).apply {
                        _productResponses.value = DataResponseState.OnSuccess(this)
                    }
                }

             }
            if (!responseCount.await().isSuccessful)
                _productResponses.value = DataResponseState.OnError(responseCount.await().errorBody().toString())

            if (!responseProductList.await().isSuccessful)
                _productResponses.value = DataResponseState.OnError(responseProductList.await().errorBody().toString())
        }
    }

    fun queryProductByTitle(searchQuery: CharSequence?=null) {
        viewModelScope.launch {
            delay(500)
            if (searchQuery!=null){
                val productResult = products?.filter {product -> product.title?.startsWith(searchQuery)?:false }
                if(productResult?.isNotEmpty() == true){
                    _productResponses.value = DataResponseState.OnSuccess(
                        ProductUIModel(
                            products = productResult,
                            count = Count(count = products?.size ?: 0)
                        )
                    )
                }else{
                    _productResponses.value = DataResponseState.OnNothingData()
                }

            }
        }

    }
}