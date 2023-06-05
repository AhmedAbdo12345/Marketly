package iti.mad.marketly.presentation.view.DiffUtils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.brands.SmartCollection


class BrandsDiffUtils: DiffUtil.ItemCallback<SmartCollection>()  {
    override fun areItemsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        Log.d("zxcvb", "areItemsTheSame: "+ (oldItem.id == newItem.id))
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        Log.d("zxcvb", "areItemsTheSame: "+ (oldItem == newItem))

        return oldItem == newItem
    }
}