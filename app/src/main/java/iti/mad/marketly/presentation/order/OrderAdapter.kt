package iti.mad.marketly.presentation.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.RvOrderBinding
import iti.mad.marketly.presentation.categoryProduct.CategoryProductDiffUtils

class OrderAdapter (var mClickListener: ListItemClickListener) : ListAdapter<Product, OrderAdapter.AdapterViewHolder>(
    OrderDiffUtils()
) {

    lateinit var binding: RvOrderBinding

    interface ListItemClickListener {
        fun onClickCategoryProduct(product: Product)
    }
    class AdapterViewHolder(var binding: RvOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvOrderBinding.inflate(inflate, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int)  {


    }
}