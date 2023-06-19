package iti.mad.marketly.presentation.draftorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.model.draftorder.DraftOrderBody
import iti.mad.marketly.data.model.draftorderresponse.DraftOrderResponse
import iti.mad.marketly.data.repository.draftorderrepo.DraftOrderRepoInterface
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class DraftOrderViewModel(private val draftOrderRepoInterface: DraftOrderRepoInterface): ViewModel() {
    private var draftResponse : MutableStateFlow<ResponseState<DraftOrderResponse>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val _draftResponse: StateFlow<ResponseState<DraftOrderResponse>> = draftResponse
    private var draftInvoice : MutableStateFlow<ResponseState<DraftOrderInvoice>> = MutableStateFlow(
        ResponseState.OnLoading(false))
    val _draftInvoice: StateFlow<ResponseState<DraftOrderInvoice>> = draftInvoice
    fun createDraftOrder(draftOrderBody: DraftOrderBody){
        viewModelScope.launch {
            draftOrderRepoInterface.createDraftOrder(draftOrderBody).flowOn(Dispatchers.IO).catch {
                draftResponse.emit(ResponseState.OnError(it.message!!))
            }.collect{
                draftResponse.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    fun sendInvoice(draftOrderInvoice: DraftOrderInvoice,draftID:String){
        viewModelScope.launch {
            draftOrderRepoInterface.sendInvoice(draftOrderInvoice,draftID).flowOn(Dispatchers.IO).catch {
                draftInvoice.emit(ResponseState.OnError(it.message!!))
            }.collect{
                draftInvoice.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    fun completeOrder(draftID:String){
        viewModelScope.launch {
            draftOrderRepoInterface.completeOrder(draftID).flowOn(Dispatchers.IO).catch {
                draftResponse.emit(ResponseState.OnError(it.message!!))
            }.collect{
                draftResponse.emit(ResponseState.OnSuccess(it))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DraftOrderViewModel(AppDependencies.draftOrderRepo)
            }
        }
    }
}