package iti.workshop.admin.data.remote.retrofit.inventory

import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.LocationListResponse
import iti.workshop.admin.data.dto.LocationSingleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InventoryLocationCallApi {

    @GET("locations.json")
    suspend fun retrieveListOfLocations():Response<LocationListResponse>
    @GET("locations/{location_id}.json")
    suspend fun retrieveSingleLocationByID(
        @Path("location_id") location_id:Long
    ):Response<LocationSingleResponse>

    @GET("locations/count.json")
    suspend fun retrieveCountLocations():Response<Count>

}