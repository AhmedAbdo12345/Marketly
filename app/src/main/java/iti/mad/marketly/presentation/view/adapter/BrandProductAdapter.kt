package iti.mad.marketly.presentation.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.brandproduct.Product
import iti.mad.marketly.databinding.RvBrandProductBinding
import iti.mad.marketly.databinding.RvHomeBrandBinding

class BrandProductAdapter (var mClickListener: BrandProductAdapter.ListItemClickListener) : ListAdapter<Product, BrandProductAdapter.BrandProductViewHolder>(BrandProductDiffUtils()) {

    lateinit var binding: RvBrandProductBinding

    interface ListItemClickListener {
        fun onClickProduct(product: Product)
    }
    class BrandProductViewHolder(var binding: RvBrandProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandProductViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvBrandProductBinding.inflate(inflate, parent, false)
        return BrandProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandProductViewHolder, position: Int)  {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgvBrandProduct)
        }

        holder.binding.tvBrandProductName.text= getItem(position).vendor
        holder.binding.tvProductName.text=getItem(position).title

        holder.binding.productModel = getItem(position)
        holder.binding.action= mClickListener


    }
}