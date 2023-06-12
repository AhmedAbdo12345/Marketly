package iti.mad.marketly.data.model.productDetails

import iti.mad.marketly.data.model.brandproduct.Image
import iti.mad.marketly.data.model.brandproduct.Option

data class Product(
    var admin_graphql_api_id: String?="",
    var body_html: String?="",
    var created_at: String?="",
    var handle: String?="",
    var id: Long?=0,
    var image: Image? =null,
    var images: List<Image>? = null,
    var options: List<Option>? =null,
    var product_type: String?="",
    var published_at: String?="",
    var published_scope: String?="",
    var status: String?="",
    var tags: String?="",
    var template_suffix: Any?=null,
    var title: String?="",
    var updated_at: String?="",
    var variants: List<Variant?>?=null,
    var vendor: String?=""
)