package iti.mad.marketly.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.FragmentCategoryBinding
import iti.mad.marketly.presentation.categoryProduct.CategoryProductAdapter
import iti.mad.marketly.presentation.categoryProduct.CategoryProductViewModel
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(), CategoryProductAdapter.ListItemClickListener {
    private lateinit var viewModel: CategoryViewModel
    lateinit var viewModelCategoryProduct: CategoryProductViewModel
    lateinit var binding: FragmentCategoryBinding
    lateinit var tabLayout: TabLayout
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
    ): View? {
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
                viewModel._category.collect {
                    when (it) {
                        is ResponseState.OnSuccess -> {
                            for (x in 0 until it.response.custom_collections.size) {
                                val collectionObj = it.response.custom_collections[x]
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
                    viewModelCategoryProduct.getAllCategoryProduct(
                        categoryObj.id.toString(),
                        FirebaseAuth.getInstance().currentUser?.uid.toString()
                    )
                    viewModelCategoryProduct._categoryProduct.collect { it ->
                        when (it) {
                            is ResponseState.OnSuccess -> {
                                var adapterProduct = CategoryProductAdapter(this@CategoryFragment) {
                                    if (it.isFavourite == true) {
                                        viewModelCategoryProduct.deleteProductFromFavourite(
                                            FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                            it
                                        )

                                    } else {
                                        viewModelCategoryProduct.addProductToFavourite(
                                            FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                            it
                                        )
                                    }
                                }
                                adapterProduct.submitList(it.response)
                                binding.categoryProductRecView.apply {
                                    adapter = adapterProduct
                                    setHasFixedSize(true)
                                    layoutManager = GridLayoutManager(context, 2).apply {
                                        orientation = RecyclerView.VERTICAL
                                    }
                                }
                            }

                            is ResponseState.OnError -> {}
                            else -> {}
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}