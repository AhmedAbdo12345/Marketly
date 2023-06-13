package iti.mad.marketly.data.repository.brandproduct

import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandProductRepoImp(private val apiService: ApiService):BrandProductRepo {
    override suspend fun getBrandProduct(brandID:String): Flow<BrandProductResponse> =flow{
       emit(apiService.getBrandProductFromAPI(brandID))
    }
}