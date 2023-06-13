package iti.mad.marketly.presentation.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.RvBrandProductBinding

class FavoriteAdapter(var context: Context?) :
    ListAdapter<Product, FavouriteViewHolder>(FavouriteDiffUtil()) {
    lateinit var binding: RvBrandProductBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvBrandProductBinding.inflate(inflater, parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val current = getItem(position)
        Picasso.get().load(current.image?.src).into(binding.imgvBrandProduct)


    }
}

