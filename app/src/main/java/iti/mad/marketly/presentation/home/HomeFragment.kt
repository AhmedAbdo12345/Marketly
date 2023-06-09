package iti.mad.marketly.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.repository.adsrepo.AdsRepoImplementation
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.databinding.FragmentHomeBinding
import iti.mad.marketly.utils.AdsManager

import iti.mad.marketly.presentation.home.brands.BrandsAdapter
import iti.mad.marketly.presentation.states.AdsStats
import iti.mad.marketly.presentation.states.PricingRuleState
import iti.mad.marketly.presentation.home.ads.AdsViewModel
import iti.mad.marketly.presentation.home.brands.BrandsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), BrandsAdapter.ListItemClickListener {

    lateinit var brandsViewModel: BrandsViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var adsViewModel: AdsViewModel
    lateinit var adsRepo: AdsRepoImplementation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandsViewModel =
            ViewModelProvider(this, BrandsViewModel.Factory).get(BrandsViewModel::class.java)
        adsViewModel = ViewModelProvider(this).get(AdsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var api = RetrofitInstance.api
        adsRepo = AdsRepoImplementation(api)

        //getBrands()
        viewLifecycleOwner.lifecycleScope.launch {
            brandsViewModel.getAllBrands()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandsViewModel._brands.collect {
                    when (it) {
                        is ResponseState.OnLoading -> {}
                        is ResponseState.OnSuccess -> {
                            var brandAdapter = BrandsAdapter(this@HomeFragment)
                            brandAdapter.submitList(it.response.smart_collections)
                            binding.brandsRecView.apply {
                                adapter = brandAdapter
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(context, 2).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }

                        is ResponseState.OnSuccess -> {}
                        else -> {}
                    }
                }
            }
        }
        adsViewModel.getPricingRule(adsRepo)
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adsViewModel._pricingRule.collect {
                    when (it) {
                        is PricingRuleState.Loading -> {}
                        is PricingRuleState.Success -> {
                            //Toast.makeText(requireContext(),"${it.pricingRules.price_rules.get(0).id}",Toast.LENGTH_LONG).show()
                            AdsManager.setValue(it.pricingRules.price_rules.get(0).value)
                            launchDiscount(it.pricingRules.price_rules.get(0).id)
                        }

                        is PricingRuleState.Failed -> {
                            Log.d("PRICINGERROR", "onViewCreated: ${it}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    override fun onClickBrand(smartCollection: SmartCollection) {
        /*val action: ActionHomeFragmentToBrandProductFragment = HomeFragmentDirections.actionHomeFragmentToBrandProductFragment(smartCollection)
           Navigation.findNavController(requireView()).navigate(action)
            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()*/
        if (smartCollection != null) {
            Log.d("zxcv", "onClickBrand: 8888" + smartCollection.title)

            var action: HomeFragmentDirections.ActionHomeFragmentToBrandProductFragment =
                HomeFragmentDirections.actionHomeFragmentToBrandProductFragment(smartCollection)
            findNavController().navigate(action)

        }
    }

    fun getBrands() {
      /*  viewLifecycleOwner.lifecycleScope.launch {
            brandsViewModel.getAllBrands()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandsViewModel._brands.collect {
                    when (it) {
                        is ResponseState.OnLoading -> {}
                        is ResponseState.OnSuccess -> {
                            var brandAdapter = BrandsAdapter(this@HomeFragment)
                            brandAdapter.submitList(it.response.smart_collections)
                            binding.brandsRecView.apply {
                                adapter = brandAdapter
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(context, 2).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }

                        is ResponseState.OnSuccess -> {}
                        else -> {}
                    }
                }
            }
        }*/
    }

    fun launchDiscount(priceRule: Long) {
        adsViewModel.getDiscount(adsRepo, priceRule)
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adsViewModel._discount.collect {
                    when (it) {
                        is AdsStats.Loading -> {
                            Toast.makeText(requireContext(), "LOOOOOAAADO", Toast.LENGTH_LONG)
                                .show()
                        }

                        is AdsStats.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "${it.discountResponce.discount_codes.size}",
                                Toast.LENGTH_LONG
                            ).show()
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
                                    AdsManager.setClipBoard(AdsManager.adsList.get(0).code)
                                    Toast.makeText(
                                        requireContext(),
                                        AdsManager.adsList.get(0).code, Toast.LENGTH_LONG
                                    ).show()
                                    if (AdsManager.useCode(AdsManager.adsList.get(0).code)) {
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
                            Log.d("PRICINGERROR", "onViewCreated: ${it}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}