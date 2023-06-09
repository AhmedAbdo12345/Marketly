package iti.mad.marketly.presentation.productdetails.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import iti.mad.marketly.data.model.productDetails.Image
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.databinding.FragmentProductDetailsBinding
import iti.mad.marketly.presentation.productdetails.Reviewer
import iti.mad.marketly.presentation.productdetails.ReviewsAdapter
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch
import kotlin.math.abs


class ProductDetailsFragment : Fragment() {
    private val viewModel by viewModels<ProductDetailsViewModel> {
        ProductDetailsViewModel.Factory
    }
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var sliderHandler: Handler
    private lateinit var sliderImages: List<Image>
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val args by navArgs<ProductDetailsFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sliderHandler = Handler(Looper.getMainLooper())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetails.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            renderDataOnScreen(uiState.response)
                        }

                        is ResponseState.OnLoading -> {
                            //todo
                        }
                        is ResponseState.OnError -> {
                            //todo
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductDetails(args.productID)
        reviewsAdapter = ReviewsAdapter(requireContext())
        val reviewsList = listOf(
            Reviewer(
                "Emad Nashaat",
                2.5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSa69_HGc_i3MXKCPZzCfAjBZC4bXJsn0rS0Ufe6H-ctZz5FbIVaPkd1jCPTpKwPruIT3Q&usqp=CAU",
                "the material wasn't good"
            ),
            Reviewer(
                "Asmaa Galal",
                3.0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5WYwjOWQCZ3g-4zrImh6lIDS7q50T4sE7S-_OeljDtY3M6pqAYqFNpKzi-t33hglOahk&usqp=CAU",
                "good product but the packaging was bad"
            ),
            Reviewer(
                "Hoda Ahmed",
                4.0,
                "https://cdn3.vectorstock.com/i/1000x1000/88/92/face-young-woman-in-frame-circular-avatar-vector-28828892.jpg",
                "Very good quality like the description"
            ),
        )
        reviewsAdapter.submitList(reviewsList)
        binding.reviewsRecycler.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private var sliderRunnable = Runnable {
        if (binding.productImageProductDetailsPage.currentItem == sliderImages.size - 1) {
            binding.productImageProductDetailsPage.currentItem = 0
        } else {
            binding.productImageProductDetailsPage.currentItem++
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    private fun handleUIViewPager(images: List<Image>) {
        sliderImages = images
        binding.productImageProductDetailsPage.adapter = ImagesAdapter(images)
        binding.productImageProductDetailsPage.clipToPadding = false
        binding.productImageProductDetailsPage.clipChildren = false
        binding.productImageProductDetailsPage.offscreenPageLimit = 3
        binding.productImageProductDetailsPage.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.productImageProductDetailsPage.setPageTransformer(compositePageTransformer)
        binding.productImageProductDetailsPage.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        TabLayoutMediator(
            binding.tabLayout, binding.productImageProductDetailsPage
        ) { tab: TabLayout.Tab?, position: Int -> }.attach()
    }

    private fun renderDataOnScreen(it: ProductDetails) {
        if (it.product?.images != null) {
            handleUIViewPager(it.product.images)
            val words = it.product.title?.lowercase()?.split(" ")
            var productName = ""
            words?.forEach { word ->
                productName += word.replaceFirstChar { it.uppercase() } + " "
            }
            binding.productNameProductDetailsPage.text = it.product.title?.trim()
            binding.productPriceProductDetailsPage.text = it.product.variants?.get(0)?.price ?: ""
            binding.productDesProductDetailsPage.text = it.product.body_html

        }

    }


}