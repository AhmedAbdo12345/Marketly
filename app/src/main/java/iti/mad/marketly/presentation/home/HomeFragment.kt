package iti.mad.marketly.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentHomeBinding
import iti.mad.marketly.presentation.home.ads.AdsViewModel
import iti.mad.marketly.presentation.home.brands.BrandsAdapter
import iti.mad.marketly.presentation.home.brands.BrandsViewModel
import iti.mad.marketly.presentation.states.AdsStats
import iti.mad.marketly.presentation.states.PricingRuleState
import iti.mad.marketly.utils.AdsManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), BrandsAdapter.ListItemClickListener {

    private lateinit var brandsViewModel: BrandsViewModel
    lateinit var binding: FragmentHomeBinding
    private val adsViewModel by viewModels<AdsViewModel> {
        AdsViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandsViewModel =
            ViewModelProvider(this, BrandsViewModel.Factory).get(BrandsViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBrands()
        adsViewModel.getPricingRule()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adsViewModel._pricingRule.collect {
                    when (it) {
                        is PricingRuleState.Loading -> {
                            binding.brandsRecView.visibility = View.GONE
                            binding.homeProgressbar.visibility = View.VISIBLE
                        }
                        is PricingRuleState.Success -> {
                            binding.brandsRecView.visibility = View.VISIBLE
                            binding.homeProgressbar.visibility = View.GONE
                            //Toast.makeText(requireContext(),"${it.pricingRules.price_rules.get(0).id}",Toast.LENGTH_LONG).show()
                            AdsManager.setValue(it.pricingRules.price_rules[0].value)
                            launchDiscount(it.pricingRules.price_rules[0].id)
                            Log.d("IDDD",SharedPreferenceManager.getUserID(requireContext())!!)
                        }

                        is PricingRuleState.Failed -> {
                            binding.brandsRecView.visibility = View.GONE
                            binding.homeProgressbar.visibility = View.GONE

                            Log.d("PRICINGERROR", "onViewCreated: $it")
                        }

                    }
                }
            }
        }
        binding.appBarHome.txtInputEditTextSearch.setOnClickListener{
            val action =HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
            Log.d("mmmms","navigate")
        }


    }

    override fun onClickBrand(smartCollection: SmartCollection) {
        if (smartCollection != null) {
            Log.d("zxcv", "onClickBrand: 8888" + smartCollection.title)

            val action: HomeFragmentDirections.ActionHomeFragmentToBrandProductFragment =
                HomeFragmentDirections.actionHomeFragmentToBrandProductFragment(smartCollection.id!!)
            findNavController().navigate(action)

        }
    }

    private fun getBrands() {
        viewLifecycleOwner.lifecycleScope.launch {
            brandsViewModel.getAllBrands()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandsViewModel.brands.collect {
                    when (it) {
                        is ResponseState.OnLoading -> {}
                        is ResponseState.OnSuccess -> {
                            val brandAdapter = BrandsAdapter(this@HomeFragment)
                            brandAdapter.submitList(it.response.smart_collections)
                            binding.brandsRecView.apply {
                                adapter = brandAdapter
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(context, 2).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun launchDiscount(priceRule: Long) {
        adsViewModel.getDiscount(priceRule)
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adsViewModel._discount.collect {
                    when (it) {
                        is AdsStats.Loading -> {

                        }

                        is AdsStats.Success -> {

                            AdsManager.addDiscountList(it.discountResponce.discount_codes)
                            val imgList = ArrayList<SlideModel>()
                            imgList.add(
                                SlideModel(
                                    "https://cdn.vectorstock.com/i/1000x1000/01/58/summer-sales-seasonal-special-offer-advertisement-vector-25930158.webp",
                                    "Summer Sales",
                                    ScaleTypes.FIT
                                )
                            )
                            imgList.add(
                                SlideModel(
                                    "https://cdn.vectorstock.com/i/1000x1000/25/02/great-winter-sales-neon-banner-vector-44882502.webp",
                                    "Summer Sales",
                                    ScaleTypes.FIT
                                )
                            )
                            binding.imageSlider.setImageList(imgList)
                            binding.imageSlider.setItemClickListener(object : ItemClickListener {
                                override fun onItemSelected(position: Int) {
                                    AdsManager.setClipBoard(AdsManager.adsList[0].code)
                                    Toast.makeText(
                                        requireContext(),
                                        AdsManager.adsList[0].code,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    if (AdsManager.useCode(AdsManager.adsList[0].code)) {
                                        Toast.makeText(
                                            requireContext(),
                                            "You can use The code",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "You can't use The Code",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            })
                        }

                        is AdsStats.Failed -> {
                            Log.d("PRICINGERROR", "onViewCreated: $it")
                        }
                    }
                }
            }
        }
    }
}











