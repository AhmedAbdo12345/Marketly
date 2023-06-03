package iti.workshop.admin.data.remote.retrofit.inventory

import iti.workshop.admin.data.dto.InventoryItemResponseAndRequest
import iti.workshop.admin.data.dto.InventoryItemsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface InventoryItemCallApi {
    @GET("inventory_items.json")
    suspend fun retrievesDetailedListForInventoryItemsByIDs(
        @Query("ids") ids:List<Long>,
        @Query("limit") limit:Int?=null
    ):Response<InventoryItemsResponse>

    @GET("inventory_items/{inventory_item_id}.json")
    suspend fun retrievesSingleInventoryItemByID(
        @Query("inventory_item_id") inventory_item_id:Long,
    ): InventoryItemResponseAndRequest


    @PUT("inventory_items/{inventory_item_id}.json")
    suspend fun updatesExistingInventoryItem(
        @Query("inventory_item_id") inventory_item_id:Long,
        @Body data:InventoryItemResponseAndRequest
    ): InventoryItemResponseAndRequest



}