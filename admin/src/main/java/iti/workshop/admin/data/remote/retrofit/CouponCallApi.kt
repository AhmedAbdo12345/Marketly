package iti.workshop.admin.data.remote.retrofit

import iti.workshop.admin.data.dto.Count
import retrofit2.Response
import retrofit2.http.GET

const val MAIN_ENDPOINT = "discount_codes"
interface CouponCallApi {
    @GET("$MAIN_ENDPOINT/count.json")
    suspend fun getCount(): Response<Count>
}