package iti.mad.marketly.data.repository.brandproduct

import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import kotlinx.coroutines.flow.Flow

interface BrandProductRepo {
    suspend fun getBrandProduct(): Flow<BrandProductResponse>
}