package iti.mad.marketly.presentation.home.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad.marketly.data.repository.adsrepo.AdsRepoInterface
import iti.mad.marketly.presentation.states.AdsStats
import iti.mad.marketly.presentation.states.PricingRuleState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdsViewModel: ViewModel() {
    private var discount : MutableStateFlow<AdsStats> = MutableStateFlow(AdsStats.Loading())
    val _discount: StateFlow<AdsStats> = discount
    private var pricingRule:MutableStateFlow<PricingRuleState> = MutableStateFlow(PricingRuleState.Loading())
    val _pricingRule:StateFlow<PricingRuleState> = pricingRule
    fun getDiscount(adsRepo:AdsRepoInterface,pricingRule:Long){
        viewModelScope.launch {
            adsRepo.getDiscount(pricingRule).flowOn(Dispatchers.IO).catch {
                discount.emit(AdsStats.Failed(it.localizedMessage))
            }.collect{
                discount.emit(AdsStats.Success(it))
            }
        }
    }
    fun getPricingRule(adsRepo: AdsRepoInterface){
        viewModelScope.launch {
            adsRepo.getPricingRules().flowOn(Dispatchers.IO).catch {
                pricingRule.emit(PricingRuleState.Failed(it.localizedMessage))
            }.collect{
                pricingRule.emit(PricingRuleState.Success(it))
            }
        }
    }
}