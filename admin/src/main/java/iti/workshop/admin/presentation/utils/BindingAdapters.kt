package iti.workshop.admin.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("thumbnail")
fun bindingImage(imageView: ImageView, urlString: String?) {
    urlString?.let {
        Glide.with(imageView.context)
            .load(urlString)
            .apply( RequestOptions())
            .into(imageView)
    }
}