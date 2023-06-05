package iti.mad.marketly.presentation.view.states

import iti.mad.marketly.data.model.brands.BrandsResponse

sealed class BrandApiStatus {
    data class Success(val brandsResponse: BrandsResponse) : BrandApiStatus()
    data class Failed(val msg :String ): BrandApiStatus()
    class Loading : BrandApiStatus()
}