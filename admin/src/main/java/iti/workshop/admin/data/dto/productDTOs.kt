package iti.workshop.admin.data.dto

import java.io.Serializable

data class SuccessProductResponse(
    val products: List<Product>?=null
)
data class UpdateProduct(
    val product: Product
)
data class PostProduct(
    val product: Product
)

data class Product(
    val id: Long=-1L,

    var title: String?=null,
    var body_html: String?=null,

    var admin_graphql_api_id: String?=null,
    var handle: String?=null,
    var image: AddImage?=null,

    var product_type: String?=null,
    var published_scope: String?=null,
    var status: String?=null,
    var tags: String?=null,
    var vendor: String?=null,

    var published_at: String?=null,
    var created_at: String?=null,
    var updated_at: String?=null,

    var images: List<Image>?=null,
    var variants: List<Variant>?=null,
    var options: List<Option>?=null,

    var metafields: List<Metafield> = listOf( Metafield() ),

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
