package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.dto.SuccessProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductCallApi {
    @GET("products.json")
    fun getProduct():Response<SuccessProductResponse>

}