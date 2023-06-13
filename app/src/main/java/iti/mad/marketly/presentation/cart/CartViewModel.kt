package iti.mad.marketly.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.cart.CartRepoInterface
import iti.mad.marketly.presentation.home.ads.AdsViewModel
import iti.mad.marketly.presentation.states.AdsStats
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepoInterface: CartRepoInterface): ViewModel() {

    private var cartResponse : MutableStateFlow<ResponseState<List<Product>>> = MutableStateFlow(ResponseState.OnLoading())
    val _cartResponse: StateFlow<ResponseState<List<Product>>> = cartResponse

    fun saveCart(product: Product){
        cartRepoInterface.saveCartProduct(product)
    }
    fun getAllCart(){
        viewModelScope.launch {
            cartRepoInterface.getAllCartProducts().flowOn(Dispatchers.IO).catch {
                cartResponse.emit(ResponseState.OnError(it.localizedMessage))
            }.collect{
                cartResponse.emit(ResponseState.OnSuccess(it))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CartViewModel(AppDependencies.cartRepoImplementation)
            }
        }
    }
}

