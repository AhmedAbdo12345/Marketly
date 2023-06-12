package iti.mad.marketly.presentation.reviews.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.databinding.FragmentReviewsBinding
import iti.mad.marketly.presentation.reviews.adapters.ReviewsAdapter

class ReviewsFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val args: ReviewsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBar.toolbar)
        binding.appBar.toolbar.title = getString(R.string.reviews)
        binding.appBar.backArrow.setOnClickListener { findNavController().popBackStack() }
        reviewsAdapter = ReviewsAdapter(requireContext())
        val linearManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.reviewsRecycler.apply {
            adapter = reviewsAdapter
            layoutManager = linearManager
        }
        reviewsAdapter.submitList(args.reviewers.toList())
    }


}