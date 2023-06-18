package iti.mad.marketly.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.search.SearchRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepo: SearchRepo) : ViewModel() {
    private var _resultProducts: MutableStateFlow<ResponseState<List<Product>>> =
        MutableStateFlow(ResponseState.OnLoading(false))
    var resultProduct: StateFlow<ResponseState<List<Product>>> = _resultProducts
    private var _noResult = MutableStateFlow<Boolean>(false)
    val noResult: StateFlow<Boolean> = _noResult
    private lateinit var products: List<Product>

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
        if (searchName.isEmpty()) {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    AppDependencies.searchRepo
                )
            }
        }
    }
}