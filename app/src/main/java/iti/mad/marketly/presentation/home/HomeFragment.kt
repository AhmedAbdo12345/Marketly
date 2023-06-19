package iti.mad.marketly.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
import iti.mad.marketly.R
import iti.mad.marketly.data.model.brands.SmartCollection
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentHomeBinding
import iti.mad.marketly.presentation.cart.CartViewModel
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
    private var cartItems: MutableList<CartModel> = mutableListOf()

    private lateinit var cartViewModel: CartViewModel


    private lateinit var brandsViewModel: BrandsViewModel
    lateinit var binding: FragmentHomeBinding
    private val adsViewModel by viewModels<AdsViewModel> {
        AdsViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandsViewModel =
            ViewModelProvider(this, BrandsViewModel.Factory).get(BrandsViewModel::class.java)

        cartViewModel =
            ViewModelProvider(this, CartViewModel.Factory).get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        displayToolBar()

        //  setHasOptionsMenu(true);


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                BadgeNotification(menu, menuInflater)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d("cart", "navigate")
                return when (menuItem.itemId) {
                    R.id.cartIcon -> {
                        // Handle the item click here
                        /*Log.d("cart","navigate")
                        findNavController().navigate(R.id.cartFragment)*/
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
                            Log.d("IDDD", SharedPreferenceManager.getUserID(requireContext())!!)
                        }

                        is PricingRuleState.Failed -> {
                            binding.brandsRecView.visibility = View.GONE
                            binding.homeProgressbar.visibility = View.GONE

                            Log.d("PRICINGERROR", "onViewCreated: $it")
                        }

                        else -> {}
                    }
                }
            }
        }/*    binding.appBarHome.txtInputEditTextSearch.setOnClickListener{
                val action =HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
                Log.d("mmmms","navigate")
            }*/


    }

    override fun onClickBrand(smartCollection: SmartCollection) {
        Log.d("zxcv", "onClickBrand: 8888" + smartCollection.title)

        val action: HomeFragmentDirections.ActionHomeFragmentToBrandProductFragment =
            HomeFragmentDirections.actionHomeFragmentToBrandProductFragment(smartCollection.id!!)
        findNavController().navigate(action)

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

                        else -> {}
                    }
                }
            }
        }
    }


    /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.cartIcon -> {
                 // Handle the item click here
                 findNavController().navigate(R.id.cartFragment2)
                 return true
             }

             else -> return super.onOptionsItemSelected(item)
         }
         *//*   if (item.getItemId()== R.id.profile){
            startActivity(new Intent(getApplicationContext() , ProfileActivity.class));

        }*//*

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      // inflater.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)

        BadgeNotification(menu,inflater)
    }*/

    private fun displayToolBar() {
        val toolbar = binding.toolbarHome

        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.supportActionBar?.title = "Home"
        activity.setSupportActionBar(toolbar)

    }

    fun BadgeNotification(menu: Menu, inflater: MenuInflater) {
        //-------- get number of Proudect from database and display in badge notification for cart icon-----------------
        inflater.inflate(R.menu.cart, menu)
        val menuItem = menu.findItem(R.id.cartIcon)
        cartViewModel.getAllCart()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel._cartResponse.collect {
                    when (it) {
                        is ResponseState.OnLoading -> {}
                        is ResponseState.OnSuccess -> {
                            cartItems = it.response.toMutableList()
                            if (cartItems.size == 0) {
                                menuItem.actionView = null
                            } else {
                                menuItem.setActionView(R.layout.badge_notification)
                                val view = menuItem.actionView
                                val badgeCounter =
                                    view!!.findViewById<TextView>(R.id.tv_badge_counter)
                                badgeCounter.text = cartItems.size.toString()
                                view.setOnClickListener {
                                    findNavController().navigate(R.id.cartFragment)
                                }
                            }
                        }

                        is ResponseState.OnError -> {}
                        else -> {}
                    }

                }
            }

        }
    }

}











