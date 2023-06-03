package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.BrandsResponse
import iti.mad.marketly.data.source.remote.retrofit.CallApi

class BrandsRepoImpl(val callApi: CallApi) : BrandsRepo {
    override suspend fun getBrands(): BrandsResponse = callApi.getBrandsFromAPI()

}