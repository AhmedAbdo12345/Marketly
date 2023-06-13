package iti.workshop.admin.presentation.features.coupon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.data.dto.DiscountCodeListResponse
import iti.workshop.admin.data.dto.DiscountCodeRequestAndResponse
import iti.workshop.admin.data.dto.PriceRule
import iti.workshop.admin.data.dto.PriceRuleCodeListResponse
import iti.workshop.admin.data.dto.PriceRuleRequestAndResponse
import iti.workshop.admin.data.repository.ICouponRepository
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.handleDate
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val _repo:ICouponRepository
):ViewModel() {

    private val _discountCodeResponse = MutableStateFlow<DataListResponseState<DiscountCodeListResponse>>(
        DataListResponseState.OnLoading())
    val discountCodeResponse: StateFlow<DataListResponseState<DiscountCodeListResponse>> get() = _discountCodeResponse



    private val _priceRuleResponse = MutableStateFlow<DataListResponseState<PriceRuleCodeListResponse>>(DataListResponseState.OnLoading())
    val priceRuleResponse:StateFlow<DataListResponseState<PriceRuleCodeListResponse>> get() = _priceRuleResponse


    private val _priceRuleActionResponse = MutableStateFlow<Pair<Boolean?,String?>>(Pair(null,null))
    val priceRuleActionResponse:StateFlow<Pair<Boolean?,String?>> get() = _priceRuleActionResponse


    private val _discountCodeActionResponse = MutableStateFlow<Pair<Boolean?,String?>>(Pair(null,null))
    val discountCodeActionResponse:StateFlow<Pair<Boolean?,String?>> get() = _discountCodeActionResponse





    fun retrievePriceRules(){
        viewModelScope.launch {
            val data = async { _repo.getPriceRules() }
            if (data.await().isSuccessful) {
                val response = data.await().body() ?: PriceRuleCodeListResponse()
                if (response.price_rules.isNotEmpty()) {
                    response.price_rules = response.price_rules.map { item-> item.copy(
                        starts_at = handleDate(item.starts_at),
                        ends_at = handleDate(item.ends_at),
                        created_at = handleDate(item.created_at),
                        updated_at = handleDate(item.updated_at)
                    ) }
                    _priceRuleResponse.value = DataListResponseState.OnSuccess(response)
                }else{
                    _priceRuleResponse.value = DataListResponseState.OnNothingData()
                }
            }else{
                _priceRuleResponse.value = DataListResponseState.OnError(data.await().errorBody().toString())
            }

        }
    }

    fun retrieveDiscountRules(priceRuleId:Long){
        viewModelScope.launch {
            val data = async { _repo.getDiscounts(priceRuleId) }
            if (data.await().isSuccessful) {
                val response = data.await().body() ?: DiscountCodeListResponse()
                if (response.discount_codes.isNotEmpty()) {
                    response.discount_codes = response.discount_codes.map { item-> item.copy(
                        created_at = handleDate(item.created_at),
                        updated_at = handleDate(item.updated_at)
                    ) }
                    _discountCodeResponse.value = DataListResponseState.OnSuccess(response)
                }else{
                    _discountCodeResponse.value = DataListResponseState.OnNothingData()
                }
            }else{
                _discountCodeResponse.value = DataListResponseState.OnError(data.await().errorBody().toString())
            }
        }
    }

    fun savePriceRule(model: PriceRule) {
        viewModelScope.launch {
            val response = async { _repo.addPriceRule(PriceRuleRequestAndResponse(price_rule = model)) }
            if (response.await().isSuccessful){
                _priceRuleActionResponse.value = Pair(true,"price rule added successfully")
                retrievePriceRules()
            }else{
                _priceRuleActionResponse.value = Pair(false,response.await().errorBody()?.string())
            }
        }
    }



    fun saveDiscountCode(model: DiscountCode) {

        viewModelScope.launch {
            val response = async { _repo.addDiscount(DiscountCodeRequestAndResponse(discount_code = model)) }
            if (response.await().isSuccessful){
                _discountCodeActionResponse.value = Pair(true,"price rule added successfully")
                retrieveDiscountRules(model.price_rule_id)
            }else{
                _discountCodeActionResponse.value = Pair(false,response.await().errorBody()?.string())

            }
        }
    }

    fun deleteDiscountCode(model: DiscountCode) {
        viewModelScope.launch {
            val response = async { _repo.deleteDiscount(model.price_rule_id,model.id) }
            if (response.await().isSuccessful){
                _discountCodeActionResponse.value = Pair(true,"discount code deleted successfully")
                retrieveDiscountRules(model.price_rule_id)
            }else{
                _discountCodeActionResponse.value = Pair(false,response.await().errorBody()?.string())
            }
        }
    }


    fun deletePriceRule(model: PriceRule) {
        viewModelScope.launch {
            val response = async { _repo.deletePriceRule(model.id) }
            if (response.await().isSuccessful){
                _discountCodeActionResponse.value = Pair(true,"price rule deleted successfully")
                retrievePriceRules()
            }else{
                _discountCodeActionResponse.value = Pair(false,response.await().errorBody()?.string())
            }
        }
    }





}