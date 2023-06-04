package iti.mad.marketly.presentation.view

import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.model.brands.BrandsResponse

sealed class BrandProductApiStatus{
    data class Success(val brandProductResponse: BrandProductResponse) : BrandProductApiStatus()
    data class Failed(val msg :String ): BrandProductApiStatus()
    class Loading :BrandProductApiStatus()
}