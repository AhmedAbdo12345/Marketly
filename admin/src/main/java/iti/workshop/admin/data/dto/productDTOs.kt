package iti.workshop.admin.data.dto

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Random

data class SuccessProductResponse(
    val products: List<Product>?=null
)
data class UpdateProduct(
    val product: Product
)
data class PostProduct(
    val product: Product
)

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var idDB:Int?=null,
    var id: Long=-1L,

    var title: String?=null,
    var body_html: String?=null,

    var admin_graphql_api_id: String?=null,
    var handle: String?=null,

    @Ignore
    var image: Image?=null,

    var product_type: String?=null,
    var published_scope: String?=null,
    var status: String?=null,
    var tags: String?=null,
    var vendor: String?=null,

    var published_at: String?=null,
    var created_at: String?=null,
    var updated_at: String?=null,

    var images: MutableList<Image?>?= mutableListOf(),
    var variants: List<Variant>?= listOf(Variant(price = Random().nextInt(100).toString())),
//    @Ignore
//    var options: List<Option>?=null,
    @Ignore
    var metafields: List<Metafield> = listOf(Metafield()),
    var rating:Float = Random().nextInt(5).toFloat()


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
):Serializable
