package iti.mad.marketly.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import iti.mad.marketly.R
import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentCategoryBinding
import iti.mad.marketly.presentation.categoryProduct.CategoryProductAdapter
import iti.mad.marketly.presentation.categoryProduct.CategoryProductViewModel
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(), CategoryProductAdapter.ListItemClickListener {
    var productList: MutableList<Product>? = null

    lateinit var viewModel: CategoryViewModel
    lateinit var viewModelCategoryProduct: CategoryProductViewModel
    lateinit var binding: FragmentCategoryBinding
    lateinit var tabLayout: TabLayout
    lateinit var adapterProduct: CategoryProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, CategoryViewModel.Factory).get(CategoryViewModel::class.java)
        viewModelCategoryProduct = ViewModelProvider(
            this, CategoryProductViewModel.Factory
        ).get(CategoryProductViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = binding.tabLayout

        getCategory()
        getProductListForEachTab()

        binding.faMenu.setOnClickListener {
            if (binding.fabCap.isVisible == false) {
                binding.fabCap.visibility = View.VISIBLE
                binding.fabTShirt.visibility = View.VISIBLE
                binding.fabShoes.visibility = View.VISIBLE
                binding.faMenu.setImageResource(R.drawable.close_white)
            } else {
                binding.fabCap.visibility = View.GONE
                binding.fabTShirt.visibility = View.GONE
                binding.fabShoes.visibility = View.GONE
                binding.faMenu.setImageResource(R.drawable.filter_alt_white)
                productList?.let {
                    displayItemsInRecycleView(it)
                }
            }

        }
    }


    override fun onClickCategoryProduct(product: Product) {
        val action =
            CategoryFragmentDirections.actionCategoryFragmentToProductDetailsFragment(product.id!!)
        findNavController().navigate(action)
    }

    fun getCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllCategory()
                viewModel.category.collect {
                    when (it) {
                        is ResponseState.OnSuccess -> {
                            tabLayout.removeAllTabs()
                            for (x in 0 until it.response.custom_collections.size) {

                                var collectionObj = it.response.custom_collections[x]
                                if (x == 0) {
                                    collectionObj.title = "HOME"
                                }
                                val myTab = tabLayout.newTab()
                                    .setText(it.response.custom_collections[x].title)
                                    .setTag(collectionObj)
                                tabLayout.addTab(myTab)
                            }
                        }

                        is ResponseState.OnError -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    fun getProductListForEachTab() {

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val categoryObj = tab.tag as CustomCollection

                viewLifecycleOwner.lifecycleScope.launch {
                    if (SharedPreferenceManager.isUserLogin(requireContext())) {
                        viewModelCategoryProduct.getAllCategoryProduct(
                            categoryObj.id.toString(),
                            SharedPreferenceManager.getFirebaseUID(requireContext()) ?: ""
                        )
                    } else {
                        viewModelCategoryProduct.getAllCategoryProduct(
                            categoryObj.id.toString(),
                        )

                    }

                    viewModelCategoryProduct.categoryProduct.collect { it ->
                        when (it) {
                            is ResponseState.OnLoading -> {
                                binding.categoryProductRecView.visibility = View.GONE
                                binding.categoryProgressbar.visibility = View.VISIBLE
                            }

                            is ResponseState.OnSuccess -> {
                                binding.categoryProductRecView.visibility = View.VISIBLE
                                binding.categoryProgressbar.visibility = View.GONE
                                adapterProduct =
                                    CategoryProductAdapter(this@CategoryFragment) { product ->
                                        if (SharedPreferenceManager.isUserLogin(requireContext())) {
                                            if (product.isFavourite == true) {
                                                viewModelCategoryProduct.deleteProductFromFavourite(
                                                    SharedPreferenceManager.getFirebaseUID(
                                                        requireContext()
                                                    ) ?: "", product
                                                )


                                            } else {
                                                viewModelCategoryProduct.addProductToFavourite(
                                                    SharedPreferenceManager.getFirebaseUID(
                                                        requireContext()
                                                    ) ?: "", product
                                                )
                                            }
                                            viewModelCategoryProduct.getAllCategoryProduct(
                                                categoryObj.id.toString(),
                                                SharedPreferenceManager.getFirebaseUID(
                                                    requireContext()
                                                ) ?: ""
                                            )

                                        } else {
                                            AlertManager.functionalDialog("register",
                                                requireContext(),
                                                "you should login or register to save this in your account",
                                                {
                                                    val action =
                                                        CategoryFragmentDirections.actionCategoryFragmentToLoginFragment()
                                                    findNavController().navigate(action)
                                                }).show()
                                        }


                                    }
                                productList = it.response.toMutableList()



                                productList?.let {
                                    displayItemsInRecycleView(it)
                                    filterByAccessories(it)
                                    filterByTShirt(it)
                                    filterByShoes(it)
                                }
                            }

                            is ResponseState.OnError -> {
                                binding.categoryProductRecView.visibility = View.GONE
                                binding.categoryProgressbar.visibility = View.GONE
                            }

                            else -> {}
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun filterByAccessories(productList: MutableList<Product>) {
        binding.fabCap.setOnClickListener {
            adapterProduct.submitList(null)
            var filterList = productList.filter { it.product_type.equals("ACCESSORIES") }
            displayItemsInRecycleView(filterList)
        }
    }

    fun filterByTShirt(productList: MutableList<Product>) {
        binding.fabTShirt.setOnClickListener {
            adapterProduct.submitList(null)
            var filterList = productList.filter { it.product_type.equals("T-SHIRTS") }
            displayItemsInRecycleView(filterList)

        }
    }

    fun filterByShoes(productList: MutableList<Product>) {
        binding.fabShoes.setOnClickListener {
            adapterProduct.submitList(null)
            var filterList = productList.filter { it.product_type.equals("SHOES") }
            displayItemsInRecycleView(filterList)
        }
    }

    fun displayItemsInRecycleView(list: List<Product>) {
        if(list.isNotEmpty()){
            binding.linearEmptyFavorite.visibility = View.GONE
            binding.categoryProductRecView.visibility = View.VISIBLE
        adapterProduct.submitList(list)
        binding.categoryProductRecView.apply {
            adapter = adapterProduct
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }else{
            binding.linearEmptyFavorite.visibility = View.VISIBLE
            binding.categoryProductRecView.visibility = View.GONE
    }

    }
}