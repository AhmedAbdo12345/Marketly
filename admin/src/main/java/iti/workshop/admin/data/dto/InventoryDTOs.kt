package iti.workshop.admin.data.dto


// InventoryItems

data class InventoryItemResponseAndRequest(
    val inventory_item: iti.workshop.admin.data.dto.InventoryItem?
)
data class InventoryItemsResponse(
    val inventory_items: List<iti.workshop.admin.data.dto.InventoryItem?>?
)

data class InventoryItem(
    val admin_graphql_api_id: String?,
    val cost: String?,
    val country_code_of_origin: Any?,
    val country_harmonized_system_codes: List<Any?>?,
    val created_at: String?,
    val harmonized_system_code: Any?,
    val id: Int?,
    val province_code_of_origin: Any?,
    val requires_shipping: Boolean?,
    val sku: String?,
    val tracked: Boolean?,
    val updated_at: String?
)

// InventoryLevels
data class InventoryLevelsResponse(
    val inventory_levels: List<iti.workshop.admin.data.dto.InventoryLevel?>?
)

data class InventoryLevel(
    val admin_graphql_api_id: String?,
    val available: Int?,
    val inventory_item_id: Int?,
    val location_id: Int?,
    val updated_at: String?
)


// Location
data class LocationListResponse(
    val locations: List<iti.workshop.admin.data.dto.Location?>?
)

data class LocationSingleResponse(
    val location: iti.workshop.admin.data.dto.Location?
)



data class Location(
    val active: Boolean?,
    val address1: String?,
    val address2: Any?,
    val admin_graphql_api_id: String?,
    val city: String?,
    val country: String?,
    val country_code: String?,
    val country_name: String?,
    val created_at: String?,
    val id: Int?,
    val legacy: Boolean?,
    val localized_country_name: String?,
    val localized_province_name: String?,
    val name: String?,
    val phone: Any?,
    val province: String?,
    val province_code: String?,
    val updated_at: String?,
    val zip: String?
)