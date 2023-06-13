package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandsRepoImpl(val apiService: ApiService) : BrandsRepo {
    override suspend fun getBrands(): Flow<BrandsResponse> = flow{
       emit(apiService.getBrandsFromAPI())
    }

}