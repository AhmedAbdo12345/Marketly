package iti.mad.marketly.data.repository.brandproduct

import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.source.remote.retrofit.CallApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandProductRepoImp(val callApi: CallApi,val brandID:String):BrandProductRepo {
    override suspend fun getBrandProduct(): Flow<BrandProductResponse> =flow{
       emit(callApi.getBrandProductFromAPI(brandID))
    }
}