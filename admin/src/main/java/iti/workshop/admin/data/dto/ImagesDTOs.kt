package iti.workshop.admin.data.dto



// Product Images
data class ImagesListResponse(
    val images: List<Image?>?
)

data class ImagesSingleResponse(
    val image: Image?
)

data class Image(
    val id: Long?=null,
    val product_id: Long?=null,

    val admin_graphql_api_id: String?=null,
    val alt: String?=null,
    val position: Int?=null,
    val src: String?=null,

    val height: Int?=null,
    val width: Int?=null,

    val created_at: String?=null,
    val updated_at: String?=null,

    // val variant_ids: List<Int?>?,
)

data class PostImage(
    val image: AddImage?
)

data class AddImage(
    val alt: String?=null,
    val attachment: String?=null,
    val filename: String?=null,
    val metafields: List<Metafield?>? = listOf(Metafield()),
    val position: Int? = null,
    val src: String?="https://img.freepik.com/free-vector/warehouse-robotization-abstract-concept-vector-illustration_335657-5680.jpg",

    )