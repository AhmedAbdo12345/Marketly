package iti.mad.marketly.presentation.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.databinding.RvCartItemBinding


class CartAdapter(var context: Context?, val cartInterface:CartFragmentInterface):
    ListAdapter<CartModel, CartViewHolder>(CartDiffUtil()) {

    lateinit var binding: RvCartItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= RvCartItemBinding.inflate(inflater,parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem=getItem(position)
        val maxQuant = currentItem.quantity
        var quant = 1
        val recentPrice = currentItem.price
        Glide.with(context!!).load(currentItem.imageURL).centerCrop().into(holder.binding.cartImage)
        holder.binding.cartName.text = currentItem.title
        holder.binding.totalItemCardPrice.text = (currentItem.price * quant).toString()
        holder.binding.cartPrice.text = currentItem.price.toString()
        holder.binding.quantityTvCart.text = quant.toString()
        holder.binding.plusLayout.setOnClickListener(View.OnClickListener {
            if(quant < maxQuant ){
                quant++
                currentItem.price = recentPrice * quant
                holder.binding.quantityTvCart.text = quant.toString()
                holder.binding.totalItemCardPrice.text = (currentItem.price * quant).toString()


            }
        })
        holder.binding.minusLayout.setOnClickListener(View.OnClickListener {
            if(quant > 1){
                quant--
                currentItem.price = recentPrice * quant
                holder.binding.quantityTvCart.text = quant.toString()
                holder.binding.totalItemCardPrice.text = (currentItem.price * quant).toString()


            }
        })
        holder.binding.cartMore.setOnClickListener(View.OnClickListener {
            cartInterface.onDelete(currentItem)
        })
    }

}