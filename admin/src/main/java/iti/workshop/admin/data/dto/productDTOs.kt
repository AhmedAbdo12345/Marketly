package iti.workshop.admin.data.dto

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
    val id: Long?=null,

    var title: String?=null,
    val body_html: String?=null,

    val admin_graphql_api_id: String?=null,
    val handle: String?=null,
    val image: AddImage?=null,

    val product_type: String?=null,
    val published_scope: String?=null,
    val status: String?=null,
    val tags: String?=null,
    val vendor: String?=null,

    val published_at: String?=null,
    val created_at: String?=null,
    val updated_at: String?=null,

    val images: List<Image>?=null,
    val variants: List<Variant>?=null,
    val options: List<Option>?=null,

    val metafields: List<Metafield> = listOf( Metafield() ),

    )




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
