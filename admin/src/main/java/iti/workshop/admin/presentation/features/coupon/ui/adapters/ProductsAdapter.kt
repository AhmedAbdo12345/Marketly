package iti.workshop.admin.presentation.features.coupon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductItemProductBindingImpl


class ProductsAdapter(
    private val clickListener: ItemCouponOnCLickListener,
) : ListAdapter<Product, ProductsAdapter.MyViewHolder>(ProductsDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.product_item_product, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binder = ProductItemProductBindingImpl.bind(itemView)
        fun binder(data:Product,itemOnCLickListener: ItemCouponOnCLickListener){
                binder.dataModel = data
//                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class ProductsDiffCallback: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem.id == newItem.id &&
                oldItem.title == newItem.title

}


