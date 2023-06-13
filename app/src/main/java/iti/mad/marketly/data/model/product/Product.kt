package iti.mad.marketly.data.model.product



data class Product(
    val admin_graphql_api_id: String,
    val body_html: String,
    val created_at: String,
    val handle: String,
    val id: Long,
    val image: Image,
    val images: List<Image>,
    val options: List<Option>,
    val product_type: String,
    val published_at: String,
    val published_scope: String,
    val status: String,
    val tags: String,
    val template_suffix: Any,
    val title: String,
    val updated_at: String,
    val variants: List<Variant>,
    val vendor: String,
    var isFavourite: Boolean? = false

)

fun Product.toProductDetails():Product {
    return Product(
        this.admin_graphql_api_id,
        this.body_html,
        this.created_at,
        this.handle,
        this.id,
        this.image,
        this.images,
        this.options,
        this.product_type,
        this.published_at,
        this.published_scope,
        this.status,
        this.tags,
        this.template_suffix,
        this.title,
        this.updated_at,
        this.variants,
         this.vendor
    )
}