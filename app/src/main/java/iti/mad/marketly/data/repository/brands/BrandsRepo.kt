package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.brands.BrandsResponse
import kotlinx.coroutines.flow.Flow

interface BrandsRepo {
    suspend fun getBrands(): Flow<BrandsResponse>
}