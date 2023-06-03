package iti.mad.marketly.presentation.view

import iti.mad.marketly.data.model.BrandsResponse

sealed class BrandApiStatus {
    data class Success(val brandsResponse: BrandsResponse) : BrandApiStatus()
    data class Failed(val msg :String ): BrandApiStatus()
    class Loading :BrandApiStatus()
}