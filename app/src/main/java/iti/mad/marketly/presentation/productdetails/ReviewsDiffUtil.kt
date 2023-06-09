package iti.mad.marketly.presentation.productdetails

import androidx.recyclerview.widget.DiffUtil


class ReviewsDiffUtil: DiffUtil.ItemCallback<Reviewer>(){
    override fun areItemsTheSame(oldItem: Reviewer, newItem: Reviewer): Boolean {
        return oldItem.name==newItem.name
    }


    override fun areContentsTheSame(oldItem: Reviewer, newItem: Reviewer): Boolean {
        return oldItem==newItem
    }
}