package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.BrandsResponse

interface BrandsRepo {
    suspend fun getBrands(): BrandsResponse
}