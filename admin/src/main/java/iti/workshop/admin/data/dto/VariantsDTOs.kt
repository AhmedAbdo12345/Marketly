package iti.workshop.admin.data.dto



data class VariantListResponse(
    val variants: List<Variant?>?
)

data class VariantSingleResponseAndRequest(
    val variant: Variant?
)
data class Variant(
    // IDs
    val id: Long?=null,
    val image_id: Long?=null,
    val inventory_item_id: Long?=null,
    val product_id: Long?=null,

    // Options
    val option1: String?=null,
    val option2: String?=null,
    val option3: String?=null,

    val admin_graphql_api_id: String?=null,
    val barcode: String?=null,
    val compare_at_price: String?=null,
    val created_at: String?=null,
    val fulfillment_service: String?=null,
    val grams: Int?=null,

    val inventory_management: String?=null,
    val inventory_policy: String?=null,
    val inventory_quantity: Int?=null,
    val old_inventory_quantity: Int?=null,

    val position: Int?=null,
    val price: String?=null,
    val requires_shipping: Boolean?=null,
    val sku: String?=null,
    val taxable: Boolean?=null,
    val title: String?=null,
    val updated_at: String?=null,
    val weight: Int?=null,
    val weight_unit: String?=null
)