package iti.mad.marketly.presentation.brandProduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import iti.mad.marketly.R
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentBrandProductBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.roundToInt


class BrandProductFragment : Fragment(), BrandProductAdapter.ListItemClickListener {


    lateinit var productList: MutableList<Product>

    private lateinit var brandAdapter: BrandProductAdapter

    private val brandProductViewModel: BrandProductViewModel by viewModels {
        BrandProductViewModel.Factory
    }
    lateinit var binding: FragmentBrandProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_brand_product, container, false)
        binding = FragmentBrandProductBinding.inflate(layoutInflater, container, false)

       /* displayToolBar()*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     /*   val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search, menu)
                val searchItem = menu.findItem(R.id.search_Icon)
                val searchView = searchItem?.actionView as SearchView
                searchView.queryHint = "Search for something"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Handle search query submission
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {

                        return false
                    }
                })
               *//* searchView.setOnClickListener {
                    val action = BrandProductFragmentDirections.actionBrandProductFragmentToSearchFragment()
                    findNavController().navigate(action)
                }*//*
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.search_Icon -> {
                        val action = BrandProductFragmentDirections.actionBrandProductFragmentToSearchFragment()
                        findNavController().navigate(action)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)*/
        displaySliderFilter()
        filterProducts()

        val smartCollection = BrandProductFragmentArgs.fromBundle(requireArguments()).brandID
        if (SharedPreferenceManager.isUserLogin(requireContext())) {
            brandProductViewModel.getAllBrandProduct(
                smartCollection.toString(),
                SharedPreferenceManager.getFirebaseUID(requireContext()) ?: ""
            )
        } else {
            brandProductViewModel.getAllBrandProduct(smartCollection.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandProductViewModel.brandProduct.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnLoading -> {
                            binding.brandProductRecycleView.visibility = View.GONE
                            binding.brandProductProgressbar.visibility = View.VISIBLE
                        }

                        is ResponseState.OnSuccess -> {
                            binding.brandProductRecycleView.visibility = View.VISIBLE
                            binding.brandProductProgressbar.visibility = View.GONE
                            brandAdapter = BrandProductAdapter(this@BrandProductFragment) {
                                if (SharedPreferenceManager.isUserLogin(requireContext())) {
                                    if (it.isFavourite == true) {
                                        brandProductViewModel.deleteProductFromFavourite(
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: "", it
                                        )
                                        brandProductViewModel.getAllBrandProduct(
                                            smartCollection.toString(),
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: ""
                                        )


                                    } else {
                                        brandProductViewModel.addProductToFavourite(
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: "", it
                                        )
                                        brandProductViewModel.getAllBrandProduct(
                                            smartCollection.toString(),
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: ""
                                        )

                                    }
                                }
                                else {
                                    AlertManager.functionalDialog(
                                        "register",
                                        requireContext(),
                                        "you should login or register to save this in your account"
                                    ) {
                                        val action =
                                            BrandProductFragmentDirections.actionBrandProductFragmentToRegisterFragment()
                                        findNavController().navigate(action)
                                    }.show()
                                }
                            }
                            productList = uiState.response.toMutableList()
                            brandAdapter.submitList(uiState.response)
                            binding.brandProductRecycleView.apply {
                                adapter = brandAdapter
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(context, 2).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }

                        is ResponseState.OnError -> {
                            binding.brandProductRecycleView.visibility = View.GONE
                            binding.brandProductProgressbar.visibility = View.GONE
                            Log.d("zxcv", "onViewCreated: 88888888888888")

                        }


                        else -> {}
                    }
                }
            }
        }

    }

    override fun onClickProduct(product: Product) {
        val action = product.id?.let {
            BrandProductFragmentDirections.actionBrandProductFragmentToProductDetailsFragment(
                it
            )
        }
        findNavController().navigate(action!!)
    }


    private fun filterProducts() {
        var startValue: Int = 0
        var endValue: Int = 0
        binding.rangSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }
        /*binding.rangSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(rangeSlider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                // Responds to when slider's value is changed
                startValue = rangeSlider.values[0].toInt()
                endValue = rangeSlider.values[1].toInt()
                filterByPrice(productList, startValue, endValue)
                binding.tvStartRange.text = "${rangeSlider.values[0].toInt()}"
                binding.tvEndRange.text = "${rangeSlider.values[1].toInt()}"
                //Toast.makeText(requireContext(), " ${rangeSlider.values[0]} ${rangeSlider.values[1]}", Toast.LENGTH_SHORT).show()

            }
        })

        binding.rangSlider.addOnChangeListener { _, _, _ ->
            // Responds to when slider's value is changed

        }*/
        binding.rangSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                startValue = 0
                endValue = slider.value.toInt()
                filterByPrice(productList, startValue, endValue)
             //   binding.tvStartRange.text = "${startValue}"
                binding.tvEndRange.text = "${endValue}$"
            }
        })

        binding.rangSlider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }

    }

    private fun displaySliderFilter() {
        binding.layoutFilter.setOnClickListener {

            if (binding.framLayoutRangSlider.isVisible) {
                binding.framLayoutRangSlider.visibility = View.GONE
            } else {
                binding.framLayoutRangSlider.visibility = View.VISIBLE
                binding.brandProductRecycleView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = 120
                }
            }
        }
    }

    fun filterByPrice(productList: MutableList<Product>, min: Int, max: Int) {
        productList.let { it ->
            brandAdapter.submitList(null)
            val filterList = it.filter {
                val doubleValue = (it.variants?.get(0)?.price)?.toDouble()
                val intValue = doubleValue?.toInt()
                (intValue in min..max)
            }
            brandAdapter.submitList(filterList)
            binding.brandProductRecycleView.apply {
                adapter = brandAdapter
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2).apply {
                    orientation = RecyclerView.VERTICAL
                }
            }
        }

    }

/*    private fun displayToolBar() {
        val toolbar = binding.toolbarBrandProduct

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = "Products"
    }*/
}
