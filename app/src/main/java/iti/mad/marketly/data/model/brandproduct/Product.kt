package iti.mad.marketly.data.model.brandproduct

data class Product(
    var admin_graphql_api_id: String,
    var body_html: String,
    var created_at: String,
    var handle: String,
    var id: Long,
    var image: Image,
    var images: List<Image>,
    var options: List<Option>,
    var product_type: String,
    var published_at: String,
    var published_scope: String,
    var status: String,
    var tags: String,
    var template_suffix: Any,
    var title: String,
    var updated_at: String,
    var vendor: String,
    var isFavourite: Boolean? = false

)

fun Product.toProductDetails(): iti.mad.marketly.data.model.productDetails.Product {
    return iti.mad.marketly.data.model.productDetails.Product(
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
        null,this.vendor
    )
}