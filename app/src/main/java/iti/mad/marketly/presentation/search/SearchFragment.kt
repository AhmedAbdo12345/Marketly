package iti.mad.marketly.presentation.search

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentSearchBinding
import iti.mad.marketly.presentation.brandProduct.BrandProductFragmentDirections
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.getQueryTextChangeStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.Factory
    }
    private lateinit var binding: FragmentSearchBinding
    private val args by navArgs<SearchFragmentArgs>()
    lateinit var productAdapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultProduct.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            productAdapter = SearchAdapter(requireContext(), {
                                if (SharedPreferenceManager.isUserLogin(requireContext())) {
                                    if (it.isFavourite == true) {
                                        viewModel.deleteProductFromFavourite(
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: "", it
                                        )
                                        viewModel.getAllProducts()


                                    } else {
                                        viewModel.addProductToFavourite(
                                            SharedPreferenceManager.getFirebaseUID(requireContext())
                                                ?: "", it
                                        )


                                    }
                                } else {
                                    AlertManager.functionalDialog(
                                        "register",
                                        requireContext(),
                                        "you should login or register to save this in your account"
                                    ) {
                                       // val action =
                                         //   SearchFragmentDirections.actionBrandProductFragmentToRegisterFragment()
                                        //findNavController().navigate(action)
                                    }.show()
                                }
                            }, {
                                it.id?.let { id ->
                                    val action =
                                        SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(
                                            id
                                        )
                                    findNavController().navigate(action)
                                }
                            })
                            val linearManager =
                                GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                            binding.recyclerView.apply {
                                adapter = productAdapter
                                layoutManager = linearManager
                            }
                            productAdapter.submitList(uiState.response)
                            Log.d("res", uiState.response.size.toString())

                        }

                        is ResponseState.OnLoading -> {
                            if (uiState.loading) {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.recyclerView.visibility = View.GONE

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
                viewModel.noResult.collect {
                    if (it) {
                        binding.noMatchResult.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.noMatchResult.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE

                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        displayToolBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllProducts()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search, menu)
                val searchItem = menu.findItem(R.id.search_Icon)
                val searchView = searchItem?.actionView as SearchView
                searchView.queryHint = "Search for something"
                lifecycleScope.launch {
                    searchView.getQueryTextChangeStateFlow().debounce(300)
                        .distinctUntilChanged().flowOn(Dispatchers.Default).collect {
                            viewModel.searchForProduct(it)

                        }
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.search_Icon -> {

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }

    private fun displayToolBar() {
        val toolbar = binding.toolbarSearch

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = "Search"
    }
}