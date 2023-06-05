package iti.mad.marketly.presentation.home.brands

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.brands.SmartCollection


class BrandsDiffUtils: DiffUtil.ItemCallback<SmartCollection>()  {
    override fun areItemsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        return oldItem == newItem
    }
}