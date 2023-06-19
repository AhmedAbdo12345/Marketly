package iti.mad.marketly.presentation.home.brands

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.databinding.RvHomeBrandBinding

class BrandsAdapter (var mClickListener: ListItemClickListener) : ListAdapter<SmartCollection, BrandsAdapter.BrandViewHolder>(
    BrandsDiffUtils()
) {

    lateinit var binding: RvHomeBrandBinding

    interface ListItemClickListener {
        fun onClickBrand(smartCollection: SmartCollection)
    }
    class BrandViewHolder(var binding: RvHomeBrandBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvHomeBrandBinding.inflate(inflate, parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int)  {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgVBrand)
            /*Glide.with(context)
                .load(getItem(position).image?.src)
                .into(binding.imgVBrand)*/
        }

        holder.binding.tvBrandName.text= getItem(position).title
        holder.binding.brandModel = getItem(position)
        holder.binding.action= mClickListener

    }
}