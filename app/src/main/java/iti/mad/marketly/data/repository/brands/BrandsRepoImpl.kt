package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.source.remote.retrofit.CallApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandsRepoImpl(val callApi: CallApi) : BrandsRepo {
    override suspend fun getBrands(): Flow<BrandsResponse> = flow{
       emit(callApi.getBrandsFromAPI())
    }

}