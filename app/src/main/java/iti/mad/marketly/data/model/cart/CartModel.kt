package iti.mad.marketly.data.model.cart

data class CartModel(
    var id: Long = 0,
    var imageURL: String = "",
    var quantity: Long = 0,
    var price: Double = 0.0,
    var title: String = "",
    var variant: Long = 0 ,
    var numberOfItems:Long = 0
)
