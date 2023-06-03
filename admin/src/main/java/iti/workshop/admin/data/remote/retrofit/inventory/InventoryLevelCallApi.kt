package iti.workshop.admin.data.remote.retrofit.inventory

import iti.workshop.admin.data.dto.InventoryLevelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InventoryLevelCallApi {

    @GET("locations/{location_id}/inventory_levels.json")
    suspend fun retrieveListInventoryLevelsForLocation(
        @Path("location_id") location_id:Long
    ):Response<InventoryLevelsResponse>

    @GET("inventory_levels.json")
    suspend fun retrievesListOfInventoryLevels(
        @Query("inventory_item_ids") inventory_item_ids:Int?=null,
        @Query("limit") limit:Int?=null,
        @Query("location_ids") location_ids:Int?=null,
        @Query("updated_at_min") updated_at_min:String?=null
    ):Response<InventoryLevelsResponse>
}