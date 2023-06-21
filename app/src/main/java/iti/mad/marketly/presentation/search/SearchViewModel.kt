package iti.mad.marketly.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.search.SearchRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepo: SearchRepo, val favouriteRep: IFavouriteRepo) :
    ViewModel() {
    private var _resultProducts: MutableStateFlow<ResponseState<List<Product>>> =
        MutableStateFlow(ResponseState.OnLoading(false))
    var resultProduct: StateFlow<ResponseState<List<Product>>> = _resultProducts
    private var _noResult = MutableStateFlow<Boolean>(false)
    val noResult: StateFlow<Boolean> = _noResult
    private lateinit var products: List<Product>
    private val _addedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val addedSuccessfully: StateFlow<ResponseState<String>> = _addedSuccessfully
    private val _deletedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading(false))
    val deletedSuccessfully: StateFlow<ResponseState<String>> = _deletedSuccessfully

    fun getAllProducts() {
        viewModelScope.launch {
            _resultProducts.value = ResponseState.OnLoading(true)
            searchRepo.getAllProducts().flowOn(Dispatchers.IO).catch {
                _resultProducts.value = ResponseState.OnError(it.localizedMessage ?: "")
            }.collect {
                _resultProducts.value = ResponseState.OnSuccess(it.products)
                products = it.products
            }
        }
    }

    fun searchForProduct(searchName: String) {
        if (searchName.isEmpty() || searchName.isBlank()) {
            if (::products.isInitialized) {
                _resultProducts.value = ResponseState.OnSuccess(products)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val filtered =
                    products.filter { it.title?.startsWith(searchName, ignoreCase = true) ?: false }
                if (filtered.isEmpty()) {
                    _noResult.value = true
                } else {
                    _noResult.value = false
                    _resultProducts.value = ResponseState.OnSuccess(filtered)
                }
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
                SearchViewModel(
                    AppDependencies.searchRepo, AppDependencies.favouriteRep
                )
            }
        }
    }
}