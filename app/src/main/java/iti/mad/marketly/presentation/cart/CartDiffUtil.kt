package iti.mad.marketly.presentation.cart

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.cart.CartModel

class CartDiffUtil: DiffUtil.ItemCallback<CartModel>() {
    override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
        return oldItem == newItem
    }
}