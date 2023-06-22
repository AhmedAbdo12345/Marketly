package iti.mad.marketly.presentation.productdetails.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import iti.mad.marketly.R
import iti.mad.marketly.data.model.Reviewer
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.product.Image
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentProductDetailsBinding
import iti.mad.marketly.presentation.brandProduct.BrandProductFragmentDirections
import iti.mad.marketly.presentation.cart.CartViewModel
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel
import iti.mad.marketly.presentation.reviews.adapters.ReviewsAdapter
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.CartManager
import iti.mad.marketly.utils.CurrencyConverter
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.launch
import kotlin.math.abs


class ProductDetailsFragment : Fragment() {
    private val viewModel by viewModels<ProductDetailsViewModel> {
        ProductDetailsViewModel.Factory
    }
    private val cartViewModel by viewModels<CartViewModel> {
        CartViewModel.Factory
    }
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var sliderHandler: Handler
    private lateinit var sliderImages: List<Image>
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var productDetails: ProductDetails
    private var isFavourite: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)/*   val bottomNavigationView =
               requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavView)
           bottomNavigationView?.visibility = View.GONE*/

        sliderHandler = Handler(Looper.getMainLooper())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetails.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            binding.progressBar6.visibility = View.GONE
                            binding.scrollView2.visibility = View.VISIBLE
                            binding.addToCartBackground.visibility = View.VISIBLE
                            renderDataOnScreen(uiState.response)
                            productDetails = uiState.response
                            if (SharedPreferenceManager.isUserLogin(requireContext())) {
                                viewModel.isFavourite(
                                    SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                                    productDetails.product!!
                                )
                            } else {
                                AlertManager.functionalDialog(
                                    "Alert",
                                    requireContext(),
                                    "you should Login to use this feature"
                              ,{
                                        val action =
                                            ProductDetailsFragmentDirections.actionProductDetailsFragmentToRegisterFragment()
                                        findNavController().navigate(action)
                                    }  )
                            }

                        }

                        is ResponseState.OnLoading -> {
                            if (uiState.loading) {
                                binding.progressBar6.visibility = View.VISIBLE
                                binding.scrollView2.visibility = View.GONE
                                binding.addToCartBackground.visibility = View.GONE
                            }
                        }

                        is ResponseState.OnError -> {
                            AlertManager.nonFunctionalDialog("Error", requireContext(), "")
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addedSuccessfully.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            Toast.makeText(requireContext(), uiState.response, Toast.LENGTH_LONG)
                                .show()

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavourite.collect { uiState ->
                    isFavourite = when (uiState) {
                        is ResponseState.OnSuccess -> {
                            if (uiState.response) {
                                binding.imgViewFavoriteIcon.setImageResource(R.drawable.ic_favorite)
                            } else {
                                binding.imgViewFavoriteIcon.setImageResource(R.drawable.ic_fav)
                            }
                            uiState.response

                        }

                        is ResponseState.OnLoading -> {
                            false
                        }

                        is ResponseState.OnError -> {
                            false
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
        binding.appBar.backArrow.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })
        viewModel.getProductDetails(args.productID)

        reviewsAdapter = ReviewsAdapter(requireContext())
        val reviewsList = listOf(
            Reviewer(
                "Mahmoud Sayed",
                2.5,
                "https://static.vecteezy.com/system/resources/previews/004/477/337/non_2x/face-young-man-in-frame-circular-avatar-character-icon-free-vector.jpg",
                "the material wasn't good"
            ),
            Reviewer(
                "Ahmed Abdo",
                3.0,
                "https://static.vecteezy.com/system/resources/previews/004/477/337/non_2x/face-young-man-in-frame-circular-avatar-character-icon-free-vector.jpg",
                "good product but the packaging was bad"
            ),
            Reviewer(
                "Mohamed Arfa",
                3.25,
                "https://static.vecteezy.com/system/resources/previews/004/477/337/non_2x/face-young-man-in-frame-circular-avatar-character-icon-free-vector.jpg",
                "the material wasn't good"
            ),
            Reviewer(
                "Hussien Ahmed",
                4.0,
                "https://static.vecteezy.com/system/resources/previews/004/477/337/non_2x/face-young-man-in-frame-circular-avatar-character-icon-free-vector.jpg",
                "Very good quality like the description"
            ),
        )
        reviewsAdapter.submitList(reviewsList.take(3))
        binding.reviewsRecycler.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
        binding.btnReviewsMore.setOnClickListener {
            val action =
                ProductDetailsFragmentDirections.actionProductDetailsFragmentToReviewsFragment(
                    reviewsList.toTypedArray()
                )
            findNavController().navigate(action)
        }
        binding.cvFavorite.setOnClickListener {
            if (SharedPreferenceManager.isUserLogin(requireContext())) {
                if (isFavourite) {
                    viewModel.deleteProductFromFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                        productDetails.product!!
                    )
                    viewModel.isFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                        productDetails.product!!
                    )
                } else {
                    viewModel.addProductToFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                        productDetails.product!!
                    )
                    viewModel.isFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                        productDetails.product!!
                    )

                }
            }else{
                AlertManager.functionalDialog(
                    "register",
                    requireContext(),
                    "you should login or register to save this in your account"
                    ,{
                        val action =
                            BrandProductFragmentDirections.actionBrandProductFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }).show()
            }

        }
        binding.addToCartProductDetailsPage.setOnClickListener(View.OnClickListener {
            if (SharedPreferenceManager.isUserLogin(requireContext())) {
                val cartProduct = productDetails.product
                val cartModel = CartModel(
                    cartProduct?.id!!,
                    cartProduct.image?.src!!,
                    cartProduct.variants?.get(0)?.inventory_quantity?.toLong()!!,
                    cartProduct.variants?.get(0)?.price?.toDouble()!!,
                    cartProduct?.title!!
                )
                cartViewModel.saveCart(cartModel)
                CartManager.addItemToCartList(cartModel)
                Toast.makeText(requireContext(),"Item sent to cart",Toast.LENGTH_LONG).show()
            } else {
                AlertManager.functionalDialog(
                    "Alert", requireContext(), "you should Login to use this feature"
              ,{
                        val action =
                            ProductDetailsFragmentDirections.actionProductDetailsFragmentToRegisterFragment()
                        findNavController().navigate(action)
                    }  )
            }

        })

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


    private fun renderDataOnScreen(it: ProductDetails) {
        if (it.product?.images != null) {
            sliderImages = it.product.images!!
            binding.productImageProductDetailsPage.adapter = ImagesAdapter(it.product.images!!)
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
            val words = it.product.title?.lowercase()?.split(" ")
            var productName = ""
            words?.forEach { word ->
                productName += word.replaceFirstChar { it.uppercase() } + " "
            }
            binding.productNameProductDetailsPage.text = productName.trim()

     if(SettingsManager.getCurrncy()=="EGP"){
       binding.productPriceProductDetailsPage.text= CurrencyConverter.switchToEGP(it.product.variants?.get(0)?.price.toString(), binding.productPriceProductDetailsPage.id)+" LE"
   }else{
         binding.productPriceProductDetailsPage.text= CurrencyConverter.switchToUSD(it.product.variants?.get(0)?.price.toString(), binding.productPriceProductDetailsPage.id)+" $"
   }



            binding.productDesProductDetailsPage.text = it.product.body_html

        }

    }


}
