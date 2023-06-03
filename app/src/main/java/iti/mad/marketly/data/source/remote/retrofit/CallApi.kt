package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.data.model.BrandsResponse
import retrofit2.http.GET

interface CallApi {
    @GET("smart_collections.json")
    suspend fun getBrandsFromAPI() : BrandsResponse
}