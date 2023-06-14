package iti.mad.marketly.presentation.productdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class ProductDetailsViewModel(
    private val productDetailsRepository: ProductDetailsRepository,
    private val favouriteRep: IFavouriteRepo
) : ViewModel() {

    private val _productDetails =
        MutableStateFlow<ResponseState<ProductDetails>>(ResponseState.OnLoading(false))
    val productDetails: StateFlow<ResponseState<ProductDetails>> = _productDetails
    private val _addedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val addedSuccessfully: StateFlow<ResponseState<String>> = _addedSuccessfully
    private val _deletedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val deletedSuccessfully: StateFlow<ResponseState<String>> = _deletedSuccessfully
    private val _isFavourite =
        MutableStateFlow<ResponseState<Boolean>>(ResponseState.OnLoading(false))
    val isFavourite: StateFlow<ResponseState<Boolean>> = _isFavourite
    fun getProductDetails(id: Long) {
        _productDetails.value = ResponseState.OnLoading(true)
        viewModelScope.launch {
            productDetailsRepository.getProductDetails(id).flowOn(Dispatchers.IO).catch { e ->
                _productDetails.value = ResponseState.OnError(e.localizedMessage ?: "eerrror")
                print(e.printStackTrace())
            }.collect {
                _productDetails.value = ResponseState.OnSuccess(it)
                print(it.toString())
            }

        }
    }

    fun addProductToFavourite(userID: String, product: Product) {
        viewModelScope.launch {
            favouriteRep.addProductToFavourite(userID, product).flowOn(Dispatchers.IO).catch {
                _addedSuccessfully.value = ResponseState.OnError(it.localizedMessage ?: "")
                print(it.printStackTrace())
            }.collect {
                print("essss")
                _addedSuccessfully.value = ResponseState.OnSuccess("added Successfully")
            }
        }
    }

    fun deleteProductFromFavourite(userID: String, product: Product) {
        viewModelScope.launch {
            favouriteRep.deleteFromFavourite(userID, product).flowOn(Dispatchers.IO).catch {
                _deletedSuccessfully.value = ResponseState.OnError(it.localizedMessage ?: "")
                print(it.printStackTrace())
            }.collect {
                print("essss")
                _deletedSuccessfully.value = ResponseState.OnSuccess("deleted Successfully")
            }
        }
    }

    fun isFavourite(userID: String, product: Product) {
        viewModelScope.launch {
            favouriteRep.isFavourite(product, userID).flowOn(Dispatchers.IO).catch {
                _isFavourite.value = ResponseState.OnError(it.localizedMessage ?: "")
                print(it.printStackTrace())
            }.collect {
                print("essss")
                _isFavourite.value = ResponseState.OnSuccess(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProductDetailsViewModel(
                    AppDependencies.productDetailsRepository, AppDependencies.favouriteRep
                )
            }
        }
    }

}