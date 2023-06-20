package iti.mad.marketly.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.repository.order.OrderRepo
import iti.mad.marketly.presentation.brandProduct.BrandProductViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class OrderViewmodel(val orderRepo: OrderRepo) : ViewModel() {

    private var _orderResponse : MutableStateFlow<ResponseState<List<OrderModel>>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val orderResponse: StateFlow<ResponseState<List<OrderModel>>> = _orderResponse
    fun getAllOrders(){
        viewModelScope.launch {
            orderRepo.getAllOrders().flowOn(Dispatchers.IO).catch {
                _orderResponse.emit(ResponseState.OnError(it.message!!))
            }.collect{
                _orderResponse.emit(ResponseState.OnSuccess(it))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OrderViewmodel(AppDependencies.orderRepo)
            }
        }
    }
}