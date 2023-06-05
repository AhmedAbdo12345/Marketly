package iti.mad.marketly.presentation.view.states

import iti.mad.marketly.data.model.discount.DiscountResponce
import iti.mad.marketly.data.model.pricingrules.PricingRules

sealed class PricingRuleState{
    data class Success(val pricingRules: PricingRules):PricingRuleState()
    data class Failed(val error:String):PricingRuleState()
    class Loading:PricingRuleState()
}
