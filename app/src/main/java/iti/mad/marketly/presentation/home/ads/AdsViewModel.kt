package iti.mad.marketly.presentation.home.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules
import iti.mad.marketly.data.repository.adsrepo.AdsRepoInterface
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdsViewModel(private val adsRepo:AdsRepoInterface): ViewModel() {
    private var discount : MutableStateFlow<ResponseState<DiscountResponce>> = MutableStateFlow(ResponseState.OnLoading(false))
    val _discount: StateFlow<ResponseState<DiscountResponce>> = discount
    private var pricingRule:MutableStateFlow<ResponseState<PricingRules>> = MutableStateFlow(ResponseState.OnLoading(false))
    val _pricingRule:StateFlow<ResponseState<PricingRules>> = pricingRule
    fun getDiscount(pricingRule:Long){
        viewModelScope.launch {
            adsRepo.getDiscount(pricingRule).flowOn(Dispatchers.IO).catch {
                discount.emit(ResponseState.OnError(it.localizedMessage))
            }.collect{
                discount.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    fun getPricingRule(){
        viewModelScope.launch {
            adsRepo.getPricingRules().flowOn(Dispatchers.IO).catch {
                pricingRule.emit(ResponseState.OnError(it.localizedMessage))
            }.collect{
                pricingRule.emit(ResponseState.OnSuccess(it))
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AdsViewModel(AppDependencies.adsRepoImplementation)
            }
        }
    }
}