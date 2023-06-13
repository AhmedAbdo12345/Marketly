package iti.mad.marketly.presentation.brandProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.R
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.RvBrandProductBinding

class BrandProductAdapter(
    var mClickListener: ListItemClickListener, var onClickFavourite: (product: Product) -> Unit
) : ListAdapter<Product, BrandProductAdapter.BrandProductViewHolder>(
    BrandProductDiffUtils()
) {

    lateinit var binding: RvBrandProductBinding

    interface ListItemClickListener {
        fun onClickProduct(product: Product)
    }

    class BrandProductViewHolder(var binding: RvBrandProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandProductViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvBrandProductBinding.inflate(inflate, parent, false)
        return BrandProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandProductViewHolder, position: Int) {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgvBrandProduct)
        }

        holder.binding.tvBrandProductName.text = getItem(position).vendor
        holder.binding.tvProductName.text = getItem(position).title
        holder.binding.tvProductPrice.text= getItem(position).variants!![0].price

        holder.binding.productModel = getItem(position)
        holder.binding.action = mClickListener
        holder.binding.imgVProductAddToFav.setOnClickListener {
            onClickFavourite(getItem(position))
        }
        if (getItem(position).isFavourite == true) holder.binding.imgVProductAddToFav.setImageResource(
            R.drawable.ic_favorite
        )
    }
}