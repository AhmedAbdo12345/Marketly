package iti.workshop.admin.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import iti.workshop.admin.R

@BindingAdapter("thumbnail")
fun bindingImage(imageView: ImageView, urlString: String?) {
    urlString?.let {
        Glide.with(imageView.context)
            .load(urlString)
            .apply( RequestOptions()/*.override(400, 300).placeholder(R.drawable.ic_shop)*/)
            //.error(R.drawable.ic_shop)
            .into(imageView)
    }
}