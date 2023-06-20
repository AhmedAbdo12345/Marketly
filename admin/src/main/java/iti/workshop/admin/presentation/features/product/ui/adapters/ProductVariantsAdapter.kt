package iti.workshop.admin.presentation.features.product.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductItemVariantBinding


class ProductVariantsAdapter(
    private val clickListener: ProductVariantsOnCLickListener,
    private val disableDelete: Boolean = false
) : ListAdapter<Variant, ProductVariantsAdapter.MyViewHolder>(ProductVariantsDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.product_item_variant, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = ProductItemVariantBinding.bind(itemView)
        fun binder(data:Variant,itemOnCLickListener: ProductVariantsOnCLickListener){
            if (disableDelete) {
                binder.deleteItemBtn.visibility = View.GONE
            } else {
                binder.deleteItemBtn.visibility = View.VISIBLE
            }
            binder.model = data
                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class ProductVariantsDiffCallback: DiffUtil.ItemCallback<Variant>() {
    override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean =
        oldItem.id == newItem.id &&
                oldItem.title == newItem.title

}

class ProductVariantsOnCLickListener(
    val clickListener: (model: Variant) -> Unit,
    val deleteItemListener: (model: Variant) -> Unit
) {
    fun onClick(model: Variant) = clickListener(model)
    fun onDeleteItem(model: Variant) = deleteItemListener(model)
}

