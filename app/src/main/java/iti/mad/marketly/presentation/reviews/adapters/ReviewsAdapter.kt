package iti.mad.marketly.presentation.reviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import iti.mad.marketly.databinding.ReviewRowBinding
import iti.mad.marketly.data.model.Reviewer

class ReviewsAdapter(var context: Context?) :
    ListAdapter<Reviewer, ReviewsViewHolder>(ReviewsDiffUtil()) {
    lateinit var binding: ReviewRowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ReviewRowBinding.inflate(inflater, parent, false)
        return ReviewsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val current = getItem(position)
        binding.txtViewReviewerName.text = current.name
        binding.reviewRate.rating = current.rate.toFloat()
        binding.reviewerComment.text = current.comment
        Picasso.get().load(current.img).into(binding.reviewImg)

    }
}

