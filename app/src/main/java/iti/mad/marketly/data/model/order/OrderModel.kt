package iti.mad.marketly.data.model.order

import iti.mad.marketly.data.model.cart.CartModel

data class OrderModel(var orderID:String="",var itemList:List<CartModel> = mutableListOf(),var itemCount:Int =0,var date:String = "")
