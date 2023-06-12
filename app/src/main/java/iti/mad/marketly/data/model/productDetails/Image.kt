package iti.mad.marketly.data.model.productDetails

data class Image(
    var admin_graphql_api_id: String?="",
    var alt: Any?=null,
    var created_at: String?="",
    var height: Int?=0,
    var id: Long?=0,
    var position: Int?=0,
    var product_id: Long?=0,
    var src: String?="",
    var updated_at: String?="",
    var variant_ids: List<Any?>?=null,
    var width: Int?=null
)