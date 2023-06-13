package iti.mad.marketly.data.model.product

data class Option(
    val id: Long?=null,
    val name: String?=null,
    val position: Int?=null,
    val product_id: Long?=null,
    val values: List<String?>?=null
)