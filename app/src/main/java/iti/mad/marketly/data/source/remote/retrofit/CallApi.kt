package iti.mad.marketly.data.source.remote.retrofit

import retrofit2.http.GET

interface CallApi {
    @GET("smart_collections.json")
    suspend fun getBrands()
}