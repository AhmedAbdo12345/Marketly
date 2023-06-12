package iti.mad.marketly.data.repository.brandproduct

import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandProductRepoImp(val apiService: ApiService, val brandID:String):BrandProductRepo {
   /* override suspend fun getBrandProduct(): Flow<BrandProductResponse> =flow{
      // emit(apiService.getBrandProductFromAPI(brandID))
    }*/
}