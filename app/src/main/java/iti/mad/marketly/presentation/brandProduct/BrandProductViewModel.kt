package iti.mad.marketly.presentation.brandProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.productRepository.ProductRepo
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BrandProductViewModel(
    private val brandsRepo: ProductRepo, private val favouriteRep: IFavouriteRepo
) : ViewModel() {

    private var _brandProduct: MutableStateFlow<ResponseState<List<Product>>> =
        MutableStateFlow(ResponseState.OnLoading(false))

    val brandProduct: StateFlow<ResponseState<List<Product>>> = _brandProduct
    private val _addedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val addedSuccessfully: StateFlow<ResponseState<String>> = _addedSuccessfully
    private val _deletedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val deletedSuccessfully: StateFlow<ResponseState<String>> = _deletedSuccessfully


    fun getAllBrandProduct(brandID: String, userID: String) {
        _brandProduct.value = ResponseState.OnLoading(true)
        viewModelScope.launch {
            brandsRepo.getProducts(brandID)
                .combine(favouriteRep.getAllFavouriteIDS(userID)) { r1, r2 ->
                    r1.products.map { product ->
                        if (r2.contains(product.id.toString())) {
                            product.isFavourite = true
                        }
                        product
                    }
                }.flowOn(Dispatchers.IO).catch {
                    _brandProduct.value = ResponseState.OnError(it.localizedMessage ?: "")
                }.collect{
                    _brandProduct.value = ResponseState.OnSuccess(it)
                }
        }
    }

    fun getAllBrandProduct(brandID: String) {
        _brandProduct.value = ResponseState.OnLoading(true)
        viewModelScope.launch {
            brandsRepo.getProducts(brandID).flowOn(Dispatchers.IO).catch {
                _brandProduct.value = ResponseState.OnError(it.localizedMessage ?: "")
            }.collect {
                _brandProduct.value = ResponseState.OnSuccess(it.products)
            }
        }
    }


    fun addProductToFavourite(
        userID: String, product: Product
    ) {
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


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BrandProductViewModel(
                    AppDependencies.productRepo, AppDependencies.favouriteRep
                )
            }
        }
    }
}

