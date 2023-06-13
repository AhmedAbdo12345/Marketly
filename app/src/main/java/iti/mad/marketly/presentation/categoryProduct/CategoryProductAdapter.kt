package iti.mad.marketly.presentation.categoryProduct

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.R
import iti.mad.marketly.databinding.RvCategoryProductBinding


class CategoryProductAdapter(var mClickListener: ListItemClickListener, var onClickFavourite: (product: Product) -> Unit) :
    ListAdapter<Product, CategoryProductAdapter.CategoryProductViewHolder>(
        CategoryProductDiffUtils()
    ) {

    lateinit var binding: RvCategoryProductBinding

    interface ListItemClickListener {
        fun onClickCategoryProduct(product: Product)
    }

    class CategoryProductViewHolder(var binding: RvCategoryProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvCategoryProductBinding.inflate(inflate, parent, false)
        return CategoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgvCategoryProduct)
        }
        holder.binding.tvCategoryItemName.text= getItem(position).vendor
        holder.binding.tvCategoryProductName.text = getItem(position).title
        holder.binding.tvCategoryProductPrice.text = getItem(position).variants?.get(0)!!.price
        holder.binding.categoryProductModel = getItem(position)
        holder.binding.action = mClickListener
        holder.binding.imgVProductAddToFav.setOnClickListener {
            onClickFavourite(getItem(position))
        }
        if (getItem(position).isFavourite == true) holder.binding.imgVProductAddToFav.setImageResource(
            R.drawable.ic_favorite
        )
        Log.d("zxcv", "onBindViewHolder: " + getItem(position).title)


    }
}