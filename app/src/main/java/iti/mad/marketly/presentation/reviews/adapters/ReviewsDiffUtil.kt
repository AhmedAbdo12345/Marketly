package iti.mad.marketly.presentation.reviews.adapters

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.Reviewer


class ReviewsDiffUtil: DiffUtil.ItemCallback<Reviewer>(){
    override fun areItemsTheSame(oldItem: Reviewer, newItem: Reviewer): Boolean {
        return oldItem.name==newItem.name
    }


    override fun areContentsTheSame(oldItem: Reviewer, newItem: Reviewer): Boolean {
        return oldItem==newItem
    }
}