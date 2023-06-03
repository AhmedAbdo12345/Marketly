package iti.mad.marketly.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.HomeAdsModel
import iti.mad.marketly.data.model.SmartCollection

class AdsDiffUtils : DiffUtil.ItemCallback<HomeAdsModel>()  {
    override fun areItemsTheSame(oldItem: HomeAdsModel, newItem: HomeAdsModel): Boolean {
        return oldItem.img == newItem.img
    }

    override fun areContentsTheSame(oldItem: HomeAdsModel, newItem: HomeAdsModel): Boolean {
        return oldItem == newItem
    }
}