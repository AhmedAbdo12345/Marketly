package iti.mad.marketly.presentation.home

import android.content.ContentValues
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
import iti.mad.marketly.presentation.settings.SettingsViewModel
import iti.mad.marketly.utils.AdsManager
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.NetworkConnectivityChecker
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
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
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkConnectivityChecker = NetworkConnectivityChecker(requireContext())
        if (!networkConnectivityChecker.checkForInternet()) {
            val action = HomeFragmentDirections.actionHomeFragmentToErrorFragment6()
            findNavController().navigate(action)
        }
        getSavedSettings()
        settingsViewModel.getExchangeRate()
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel._currency.collect {
                    when (it) {
                        is ResponseState.OnLoading -> {

                        }

                        is ResponseState.OnSuccess -> {

                            SettingsManager.exchangeRateSetter(it.response.conversion_rates.EGP)
                        }

                        is ResponseState.OnError -> {
                            Log.i(ContentValues.TAG, "onViewCreated:${it.message} ")
                        }

                        else -> {}
                    }
                }
            }
        }
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
                if (SharedPreferenceManager.isUserLogin(requireContext())) {
                    BadgeNotification(menu, menuInflater)
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d("cart", "navigate")
                return when (menuItem.itemId) {
                    R.id.cartIcon -> {
                        // Handle the item click here
                        /*Log.d("cart","navigate")*/
                        // binding.notLoginConstraint.visibility = View.GONE

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
                        is ResponseState.OnLoading -> {
                            binding.brandsRecView.visibility = View.GONE
                            binding.homeProgressbar.visibility = View.VISIBLE
                        }

                        is ResponseState.OnSuccess -> {
                            binding.brandsRecView.visibility = View.VISIBLE
                            binding.homeProgressbar.visibility = View.GONE
                            AdsManager.setPriceLists(it.response.price_rules.toMutableList())
                            for (rules in it.response.price_rules) {
                                launchDiscount(rules.id)
                            }

                            Log.d("IDDD", SharedPreferenceManager.getUserID(requireContext())!!)
                        }

                        is ResponseState.OnError -> {
                            binding.brandsRecView.visibility = View.GONE
                            binding.homeProgressbar.visibility = View.GONE

                            Log.d("PRICINGERROR", "onViewCreated: $it")
                        }

                        else -> {}
                    }
                }

            }
        }
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
                val ad = AdsManager.getAdsList().get(position)
                var rule = ""
                for (rules in AdsManager.getPriceList()) {
                    if (ad.price_rule_id == rules.id) {
                        rule = rules.value
                    }
                }
                val method = {
                    AdsManager.setClipBoard(ad)
                    for (rules in AdsManager.getPriceList()) {
                        if (AdsManager.getAdsList().get(0).price_rule_id == rules.id) {
                            AdsManager.setValue(rules.value)
                        }
                    }
                    Toast.makeText(
                        requireContext(),
                        AdsManager.getAdsList().get(0).code,
                        Toast.LENGTH_LONG
                    ).show()
                }
                AlertManager.customDialog(
                    ad.code,
                    requireContext(),
                    rule,
                    method
                ).show()

            }
        })


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
                        is ResponseState.OnLoading -> {

                        }

                        is ResponseState.OnSuccess -> {
                            AdsManager.appendAdd(it.response.discount_codes.toMutableList())

                        }

                        is ResponseState.OnError -> {
                            Log.d("PRICINGERROR", "onViewCreated: $it")
                        }

                        else -> {}
                    }
                }
            }
        }
    }


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
                                    findNavController().navigate(R.id.cartFragment2)
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

    private fun getSavedSettings() {
        SettingsManager.documentIDSetter(SharedPreferenceManager.getUserMAil(requireContext())!!)
        SettingsManager.userNameSetter(SharedPreferenceManager.getUserName(requireContext())!!)
        SettingsManager.addressSetter(SharedPreferenceManager.getDefaultAddress(requireContext())!!)
        SettingsManager.curSetter(SharedPreferenceManager.getSavedCurrency(requireContext())!!)
        Log.d("aaaaa7777aaaaaaa", SharedPreferenceManager.getSavedCurrency(requireContext()).toString())
        SettingsManager.exchangeRateSetter(
            SharedPreferenceManager.getDefaultExchangeRate(
                requireContext()
            )!!
        )
    }
    /*  fun handleRegister() {
          binding.loginBtn.setOnClickListener {
              val action = FavouriteFragmentDirections.actionFavouriteFragmentToLoginFragment()
              findNavController().navigate(action)
          }
          binding.registerBtn.setOnClickListener {
              val action = FavouriteFragmentDirections.actionFavouriteFragmentToRegisterFragment()
              findNavController().navigate(action)


          }
      }*/
}


/*
* AdsManager.addDiscountList(it.response.discount_codes)
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
                            })*/



