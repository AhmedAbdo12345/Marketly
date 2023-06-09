package iti.mad.marketly.data.model.productDetails

data class Option(
    val id: Long?,
    val name: String?,
    val position: Int?,
    val product_id: Long?,
    val values: List<String?>?
)