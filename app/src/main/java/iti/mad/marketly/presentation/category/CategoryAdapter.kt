package iti.mad.marketly.presentation.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.databinding.RvCategoryBinding

class CategoryAdapter (var mClickListener: ListItemClickListener) : ListAdapter<CustomCollection, CategoryAdapter.CategoryViewHolder>(
    CategoryDiffUtils()
) {

    lateinit var binding: RvCategoryBinding

    interface ListItemClickListener {
        fun onClickCategory(customCollection: CustomCollection)
    }
    class CategoryViewHolder(var binding: RvCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvCategoryBinding.inflate(inflate, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int)  {
        if (getItem(position).image?.src != null) {
            Picasso.get().load(getItem(position).image?.src).into(binding.imgVCategory)
        }
        holder.binding.tvCategoryName.text= getItem(position).title
        holder.binding.categoryModel = getItem(position)
        holder.binding.action= mClickListener


    }
}