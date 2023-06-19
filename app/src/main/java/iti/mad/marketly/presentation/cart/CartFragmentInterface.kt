package iti.mad.marketly.presentation.cart

import iti.mad.marketly.data.model.cart.CartModel

interface CartFragmentInterface {
    fun onDelete(cartModel: CartModel)
}