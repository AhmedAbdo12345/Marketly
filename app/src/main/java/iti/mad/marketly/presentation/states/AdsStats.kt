package iti.mad.marketly.presentation.states

import iti.mad.marketly.data.model.discount.DiscountResponce

sealed class AdsStats{
    data class Success(val discountResponce: DiscountResponce): AdsStats()
    data class Failed(val error:String): AdsStats()
    class Loading: AdsStats()
}
