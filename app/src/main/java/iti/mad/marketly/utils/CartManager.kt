package iti.mad.marketly.utils

import iti.mad.marketly.data.model.cart.CartModel

object CartManager {
    private lateinit var cartList: MutableList<CartModel>

    fun addItemToCartList(cartModel: CartModel){
        if(!this::cartList.isInitialized){
            cartList = mutableListOf()
            cartList.add(cartModel)
        }else{
            cartList.add(cartModel)
        }
    }
    fun getItemList():MutableList<CartModel> = cartList



}