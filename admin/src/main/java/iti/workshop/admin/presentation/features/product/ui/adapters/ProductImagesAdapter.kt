package iti.workshop.admin.presentation.features.product.ui.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.databinding.ProductItemImageBinding


class ProductImagesAdapter(
    private val clickListener: ProductImagesOnCLickListener,
    private val disableDelete: Boolean = false,
    private val isBase64: Boolean = false,
) : ListAdapter<Image, ProductImagesAdapter.MyViewHolder>(ProductImagesDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_item_image, parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = ProductItemImageBinding.bind(itemView)
        fun binder(data: Image, itemOnCLickListener: ProductImagesOnCLickListener) {
            if (disableDelete) {
                binder.productAddToFavSingleProduct.visibility = View.GONE
            } else {
                binder.productAddToFavSingleProduct.visibility = View.VISIBLE
            }
            bindingImage(binder.imageProduct,data)
            binder.model = data
            binder.clickListener = itemOnCLickListener
            binder.executePendingBindings()
        }
    }


    fun bindingImage(imageView: ImageView, data: Image) {
            if (isBase64){
                val decodedBytes = Base64.decode(data.attachment, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                imageView.setImageBitmap(bitmap)
            }else{
                Glide.with(imageView.context)
                    .load(data.src)
                    .apply( RequestOptions())
                    .into(imageView)
            }
    }
}

class ProductImagesDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem.id == newItem.id

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

