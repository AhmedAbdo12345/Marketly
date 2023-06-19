package iti.mad.marketly.presentation.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import iti.mad.marketly.R
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.RvFavouriteBinding

class SearchAdapter(
    var context: Context?,
    var onClickFavourite: (product: Product) -> Unit,
    var onClickItem: (product: Product) -> Unit
) : ListAdapter<Product, SearchViewHolder>(SearchDiffUtil()) {
    lateinit var binding: RvFavouriteBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvFavouriteBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgvCategoryProduct)
        }

        holder.binding.tvCategoryItemName.text = getItem(position).vendor
        holder.binding.tvCategoryProductName.text = getItem(position).title
        holder.binding.tvCategoryProductPrice.text = getItem(position).variants!![0]!!.price
        holder.binding.categoryProductModel = getItem(position)
        holder.binding.cardView.setOnClickListener {
            onClickItem(getItem(position))
        }
        holder.binding.imgVProductAddToFav.setOnClickListener {
            onClickFavourite(getItem(position))

        }
        if (getItem(position).isFavourite == true) {
            holder.binding.imgVProductAddToFav.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.binding.imgVProductAddToFav.setImageResource(R.drawable.ic_fav)
        }


    }
}

