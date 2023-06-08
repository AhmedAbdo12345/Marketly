package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import iti.workshop.admin.data.remote.retrofit.InventoryAPICalls
import retrofit2.Response

class ImplInventoryRepository(private val _api:InventoryAPICalls):IInventoryRepository {
    override suspend fun retrievesDetailedListForInventoryItemsByIDs(
        ids: List<Long>,
        limit: Int?
    ): Response<InventoryItemsResponse>
    = _api.inventoryItemCallApi.retrievesDetailedListForInventoryItemsByIDs(ids, limit)

    override suspend fun retrievesSingleInventoryItemByID(inventory_item_id: Long): InventoryItemResponseAndRequest
    = _api.inventoryItemCallApi.retrievesSingleInventoryItemByID(inventory_item_id)

    override suspend fun updatesExistingInventoryItem(
        inventory_item_id: Long,
        data: InventoryItemResponseAndRequest
    ): InventoryItemResponseAndRequest
    = _api.inventoryItemCallApi.updatesExistingInventoryItem(inventory_item_id, data)

    override suspend fun retrieveListInventoryLevelsForLocation(location_id: Long): Response<InventoryLevelsResponse>
    = _api.inventoryLevelCallApi.retrieveListInventoryLevelsForLocation(location_id)

    override suspend fun retrievesListOfInventoryLevels(
        inventory_item_ids: Int?,
        limit: Int?,
        location_ids: Int?,
        updated_at_min: String?
    ): Response<InventoryLevelsResponse>
     = _api.inventoryLevelCallApi.retrievesListOfInventoryLevels(inventory_item_ids, limit, location_ids, updated_at_min)

    override suspend fun retrieveListOfLocations(): Response<LocationListResponse>
    = _api.inventoryLocationCallApi.retrieveListOfLocations()

    override suspend fun retrieveSingleLocationByID(location_id: Long): Response<LocationSingleResponse>
     = _api.inventoryLocationCallApi.retrieveSingleLocationByID(location_id)

    override suspend fun retrieveCountLocations(): Response<Count>
    = _api.inventoryLocationCallApi.retrieveCountLocations()
}