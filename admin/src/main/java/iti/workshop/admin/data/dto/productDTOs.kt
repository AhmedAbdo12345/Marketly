package iti.workshop.admin.data.dto

import java.io.Serializable

data class SuccessProductResponse(
    val products: List<iti.workshop.admin.data.dto.Product>?=null
)
data class UpdateProduct(
    val product: iti.workshop.admin.data.dto.Product
)
data class PostProduct(
    val product: iti.workshop.admin.data.dto.Product
)

data class Product(
    val id: Long=-1L,

    var title: String?=null,
    var body_html: String?=null,

    var admin_graphql_api_id: String?=null,
    var handle: String?=null,
    var image: iti.workshop.admin.data.dto.AddImage?=null,

    var product_type: String?=null,
    var published_scope: String?=null,
    var status: String?=null,
    var tags: String?=null,
    var vendor: String?=null,

    var published_at: String?=null,
    var created_at: String?=null,
    var updated_at: String?=null,

    var images: List<iti.workshop.admin.data.dto.Image>?=null,
    var variants: List<iti.workshop.admin.data.dto.Variant>?=null,
    var options: List<iti.workshop.admin.data.dto.Option>?=null,

    var metafields: List<iti.workshop.admin.data.dto.Metafield> = listOf(iti.workshop.admin.data.dto.Metafield()),

    ): Serializable




data class Option(
    val id: Long?=null,
    val name: String?=null,
    val position: Int?=null,
    val product_id: Long?=null,
    val values: List<String>?=null
)





// Metafield
data class Metafield(
    val key: String=  "new",
    val namespace: String = System.currentTimeMillis().toString(),
    val type: String = "single_line_text_field",
    val value: String = "newvalue"
)
