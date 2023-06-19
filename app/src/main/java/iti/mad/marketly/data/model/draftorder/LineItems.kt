package iti.mad.marketly.data.model.draftorder

data class LineItems(



    val id: Int,

    val price: String,
    val product_id: Int,

    val quantity: Int,

    val title: String

)