package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.dto.Count
import retrofit2.Response
import retrofit2.http.GET

interface CouponCallApi {
    @GET("discount_codes/count.json")
    suspend fun getCount(): Response<Count>
}