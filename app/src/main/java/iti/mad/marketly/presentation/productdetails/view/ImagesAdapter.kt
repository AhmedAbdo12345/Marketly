package iti.mad.marketly.presentation.productdetails.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iti.mad.marketly.R
import iti.mad.marketly.data.model.productDetails.Image

class ImagesAdapter(private val images: List<Image>) : RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position].src
        Glide.with(holder.itemView).load(imageUrl).centerCrop().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

}

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageView: ImageView = itemView.findViewById(R.id.productimage)

}