package iti.workshop.admin.presentation.features.product.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.databinding.ProductItemImageBinding


class ProductImagesAdapter(
    private val clickListener: ProductImagesOnCLickListener,
) : ListAdapter<Image, ProductImagesAdapter.MyViewHolder>(ProductImagesDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.product_item_image, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = ProductItemImageBinding.bind(itemView)
        fun binder(data:Image,itemOnCLickListener: ProductImagesOnCLickListener){
                binder.model = data
                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class ProductImagesDiffCallback: DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.id == newItem.id &&
                oldItem.alt == newItem.alt

}

class ProductImagesOnCLickListener(
    val clickListener: (model: Image) -> Unit,
    val deleteItemListener: (model: Image) -> Unit
) {
    fun onClick(model: Image) = clickListener(model)
    fun onDeleteItem(model: Image) = deleteItemListener(model)
}

