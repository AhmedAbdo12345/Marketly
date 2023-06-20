package iti.mad.marketly.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.databinding.FragmentSearchBinding
import iti.mad.marketly.presentation.favourite.FavoriteAdapter
import iti.mad.marketly.presentation.getQueryTextChangeStateFlow
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
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
    lateinit var productAdapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultProduct.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility=View.VISIBLE
                            productAdapter = FavoriteAdapter(requireContext(), {
                                //TODO
                            }, {
                                //todo
                            })
                            val linearManager =
                                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllProducts()
        lifecycleScope.launch {
            binding.appBarHome.txtInputEditTextSearch.getQueryTextChangeStateFlow()
                .debounce(300)
                .distinctUntilChanged().flowOn(Dispatchers.Default).collect {
                    viewModel.searchForProduct(it)

                }
        }


    }
}