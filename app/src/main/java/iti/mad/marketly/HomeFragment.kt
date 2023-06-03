package iti.mad.marketly

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.data.model.BrandsResponse
import iti.mad.marketly.data.model.HomeAdsModel
import iti.mad.marketly.data.model.SmartCollection
import iti.mad.marketly.data.repository.brands.BrandsRepoImpl
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.databinding.FragmentHomeBinding
import iti.mad.marketly.presentation.view.BrandApiStatus
import iti.mad.marketly.presentation.view.adapter.AdsAdapter
import iti.mad.marketly.presentation.view.adapter.BrandsAdapter
import iti.mad.marketly.presentation.view.viewmodel.BrandsViewModel
import kotlinx.coroutines.launch
import kotlin.math.log


class HomeFragment : Fragment()  , BrandsAdapter.ListItemClickListener,AdsAdapter.ListItemClickListener{

     lateinit var brandsViewModel: BrandsViewModel
    lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandsViewModel = ViewModelProvider(this).get(BrandsViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var list= listOf<HomeAdsModel>(HomeAdsModel(R.drawable.bn),HomeAdsModel(R.drawable.ads_img),HomeAdsModel(R.drawable.bn))
        var adsAdapter = AdsAdapter(list,this@HomeFragment)
        binding.adsRecView.apply {
            adapter = adsAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        var api = RetrofitInstance.api
        var repo = BrandsRepoImpl(api)
        brandsViewModel.getAllBrands(repo)
        lifecycleScope.launch{
            brandsViewModel._brands.collect{
                when(it){
                    is  BrandApiStatus.Loading ->{

                    }
                    is BrandApiStatus.Success -> {
                    var brandAdapter = BrandsAdapter(this@HomeFragment)
                        brandAdapter.submitList(it.brandsResponse.smart_collections)
                        binding.brandsRecView.apply {
                            adapter = brandAdapter
                            setHasFixedSize(true)
                            layoutManager = GridLayoutManager(context, 2).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }
                    is BrandApiStatus.Failed  -> {
                        Log.d("zxcv", "onViewCreated: 88888888888888")

                    }

                    else -> {}
                }
            }

        }
    }


    override fun onClickBrand(smartCollection: SmartCollection) {
        TODO("Not yet implemented")
    }

    override fun onClickAds(homeAdsModel: HomeAdsModel) {
        TODO("Not yet implemented")
    }
}