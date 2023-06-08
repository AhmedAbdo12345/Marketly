package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import retrofit2.Response

interface IInventoryRepository {

    suspend fun retrievesDetailedListForInventoryItemsByIDs(
        ids:List<Long>,
        limit:Int?=null
    ): Response<InventoryItemsResponse>

    suspend fun retrievesSingleInventoryItemByID(
        inventory_item_id:Long,
    ): InventoryItemResponseAndRequest


    suspend fun updatesExistingInventoryItem(
        inventory_item_id:Long,
        data: InventoryItemResponseAndRequest
    ): InventoryItemResponseAndRequest




    suspend fun retrieveListInventoryLevelsForLocation(
        location_id:Long
    ):Response<InventoryLevelsResponse>

    suspend fun retrievesListOfInventoryLevels(
        inventory_item_ids:Int?=null,
        limit:Int?=null,
        location_ids:Int?=null,
        updated_at_min:String?=null
    ):Response<InventoryLevelsResponse>



    suspend fun retrieveListOfLocations():Response<LocationListResponse>
    suspend fun retrieveSingleLocationByID(
        location_id:Long
    ):Response<LocationSingleResponse>

    suspend fun retrieveCountLocations():Response<iti.workshop.admin.data.dto.Count>

}