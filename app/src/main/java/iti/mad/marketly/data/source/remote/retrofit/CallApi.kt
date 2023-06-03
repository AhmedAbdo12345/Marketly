package iti.mad.marketly.data.source.remote.retrofit

import iti.mad.marketly.data.model.brands.BrandsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CallApi {
    @GET("smart_collections.json")
    suspend fun getBrandsFromAPI() : BrandsResponse

    @GET("collections/{brand_Id}/products.json\n")
    suspend fun getBrandProductFromAPI(@Path("brand_Id") brandID: String) : BrandsResponse

}