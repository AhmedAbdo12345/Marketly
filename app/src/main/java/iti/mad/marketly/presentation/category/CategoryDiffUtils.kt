package iti.mad.marketly.presentation.category

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.category.CustomCollection

class CategoryDiffUtils : DiffUtil.ItemCallback<CustomCollection>()  {
    override fun areItemsTheSame(oldItem: CustomCollection, newItem: CustomCollection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CustomCollection, newItem: CustomCollection): Boolean {
        return oldItem == newItem
    }
}