package iti.mad.marketly.presentation.draftorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shopify.data.stripe.repository.StripeRepository
import com.example.shopify.data.stripe.response.CreateEphemeralKeyResponse
import com.example.shopify.data.stripe.response.CreatePaymentIntentResponse
import com.example.shopify.data.stripe.response.StripeCustomerResponse
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.draftorderresponse.DraftOrderResponse
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StripeViewModel(private val stripeRepo:StripeRepository): ViewModel() {
private var customerCreationResponce : MutableStateFlow<ResponseState<StripeCustomerResponse>> = MutableStateFlow(
    ResponseState.OnLoading(false))
    val _customerCreationResponce: StateFlow<ResponseState<StripeCustomerResponse>> = customerCreationResponce
    private var keyCreationResponce : MutableStateFlow<ResponseState<CreateEphemeralKeyResponse>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val _keyCreationResponce: StateFlow<ResponseState<CreateEphemeralKeyResponse>> = keyCreationResponce
    private var paymentIntent : MutableStateFlow<ResponseState<CreatePaymentIntentResponse>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val _paymentIntent: StateFlow<ResponseState<CreatePaymentIntentResponse>> = paymentIntent
fun createUser(){
    viewModelScope.launch {
        stripeRepo.createCustomer().flowOn(Dispatchers.IO).catch {
            customerCreationResponce.emit(ResponseState.OnError(it.localizedMessage))
        }.collect{
            customerCreationResponce.emit(ResponseState.OnSuccess(it))
        }
    }
}
    fun createKey(customerKey:String){
        viewModelScope.launch {
            stripeRepo.createEphemeralKey(customerKey).flowOn(Dispatchers.IO).catch {
                keyCreationResponce.emit(ResponseState.OnError(it.localizedMessage))
            }.collect{
                keyCreationResponce.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    fun createPaymentIntent(customerId: String,
                            amount: Int,
                            currency: String,
                            autoPaymentMethodsEnable: Boolean){
        viewModelScope.launch{
            stripeRepo.createPaymentIntent(customerId,amount,currency,autoPaymentMethodsEnable).flowOn(Dispatchers.IO)
                .catch { paymentIntent.emit(ResponseState.OnError(it.localizedMessage)) }
                .collect{paymentIntent.emit(ResponseState.OnSuccess(it))}
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StripeViewModel(AppDependencies.stripeRepo)
            }
        }
    }
}