package iti.mad.marketly.presentation.brandProduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentBrandProductBinding
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency


class BrandProductFragment : Fragment(), BrandProductAdapter.ListItemClickListener {


    lateinit var productList:MutableList<Product>

    lateinit var brandAdapter : BrandProductAdapter

    val brandProductViewModel: BrandProductViewModel by viewModels<BrandProductViewModel> {
        BrandProductViewModel.Factory
    }
    lateinit var binding: FragmentBrandProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_brand_product, container, false)
        binding = FragmentBrandProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displaySliderFilter()
        filterProducts()

        var smartCollection =
            BrandProductFragmentArgs.fromBundle(requireArguments()).brandID
        brandProductViewModel.getAllBrandProduct(
            smartCollection.toString(),
            SharedPreferenceManager.getFirebaseUID(requireContext()) ?: ""
        )
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandProductViewModel._brandProduct.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnLoading -> {

                        }

                        is ResponseState.OnSuccess -> {
                             brandAdapter = BrandProductAdapter(this@BrandProductFragment) {
                                if (it.isFavourite == true) {
                                    brandProductViewModel.deleteProductFromFavourite(
                                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                                        it
                                    )

                                } else {
                                    brandProductViewModel.addProductToFavourite(
                                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "",
                                        it
                                    )
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
                            Log.d("zxcv", "onViewCreated: 88888888888888")

                        }

                        else -> {}
                    }
                }
            }
        }

    }

    override fun onClickProduct(product: Product) {
        val action =
            product.id?.let {
                BrandProductFragmentDirections.actionBrandProductFragmentToProductDetailsFragment(
                    it
                )
            }
        findNavController().navigate(action!!)
    }


    fun filterProducts(){
        var startValue = 0
        var endValue = 0
        binding.rangSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }
        binding.rangSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(rangeSlider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                // Responds to when slider's value is changed
                startValue = rangeSlider.values[0].toInt()
                endValue = rangeSlider.values[1].toInt()
                filterByPrice(productList,startValue,endValue )
                binding.tvStartRange.text = "${rangeSlider.values[0].toInt()}"
                binding.tvEndRange.text = "${rangeSlider.values[1].toInt()}"
           //Toast.makeText(requireContext(), " ${rangeSlider.values[0]} ${rangeSlider.values[1]}", Toast.LENGTH_SHORT).show()

            }
        })

        binding.rangSlider.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed

        }

    }

    fun displaySliderFilter(){
        binding.layoutFilter.setOnClickListener {

            if (binding.framLayoutRangSlider.isVisible == true){
                  binding.framLayoutRangSlider.visibility =View.GONE
              } else{
                  binding.framLayoutRangSlider.visibility =View.VISIBLE
                binding.brandProductRecycleView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = 120
                }              }
           }
    }
    fun filterByPrice(productList: MutableList<Product>,  min :Int,max:Int){
        productList.let {
            brandAdapter.submitList(null)
            var  filterList =it.filter {
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
}
