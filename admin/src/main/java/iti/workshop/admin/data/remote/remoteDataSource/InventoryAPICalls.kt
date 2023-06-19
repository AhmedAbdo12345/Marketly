package iti.workshop.admin.data.remote.remoteDataSource

import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryItemCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryLevelCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryLocationCallApi

data class InventoryAPICalls(
    // Inventory
    val inventoryItemCallApi: InventoryItemCallApi = RetrofitInstance.inventoryItemCallApi,
    val inventoryLevelCallApi: InventoryLevelCallApi = RetrofitInstance.inventoryLevelCallApi,
    val inventoryLocationCallApi: InventoryLocationCallApi = RetrofitInstance.inventoryLocationCallApi,
)