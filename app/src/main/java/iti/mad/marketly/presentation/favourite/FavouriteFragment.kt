package iti.mad.marketly.presentation.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import iti.mad.marketly.R
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentFavouriteBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var navController: NavController
    private lateinit var favAdapter: FavoriteAdapter
    private lateinit var gridManager: GridLayoutManager
    private val favoriteViewModel: FavouriteViewModel by viewModels<FavouriteViewModel> {
        FavouriteViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.allFavourites.collect {
                    when (it) {
                        is ResponseState.OnSuccess -> {
                            binding.progressBar3.visibility = View.GONE
                            if (it.response.isEmpty()) {
                                binding.favRecycler.visibility = View.GONE
                                binding.linearEmptyFavorite.visibility = View.VISIBLE
                            }
                            showFavorites(it.response)
                        }

                        is ResponseState.OnLoading -> {
                            if (it.loading) {
                                binding.progressBar3.visibility = View.VISIBLE
                            }
                        }

                        is ResponseState.OnError -> {
                            //todo
                        }
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.navController = findNavController()

        if ( !SharedPreferenceManager.isUserLogin(requireContext())) {
            binding.notLoginConstraint.visibility = View.VISIBLE
            handleRegister()

        } else {
            binding.notLoginConstraint.visibility = View.GONE
            favoriteViewModel.getAllFavourite(
                SharedPreferenceManager.getFirebaseUID(requireContext()) ?: ""
            )

        }


    }

    private fun showFavorites(products: List<Product>) {
        favAdapter = FavoriteAdapter(requireContext(), {
            AlertManager.functionalDialog(
                "Alert", requireContext(), "Do You want to delete this item from favourite"
           , {
                    favoriteViewModel.deleteProductFromFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "", it
                    )
                    favoriteViewModel.getAllFavourite(
                        SharedPreferenceManager.getFirebaseUID(requireContext()) ?: ""
                    )
                } ).show()
        }, { product ->
            val action = product.id?.let {
                FavouriteFragmentDirections.actionFavouriteFragmentToProductDetailsFragment(
                    it,
                )
            }
            findNavController().navigate(action!!)
        })
        gridManager = GridLayoutManager(requireContext(), 2)
        binding.favRecycler.layoutManager = gridManager
        binding.favRecycler.adapter = favAdapter
        favAdapter.submitList(products)

    }

    fun handleRegister() {
        binding.loginBtn.setOnClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.registerBtn.setOnClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToRegisterFragment()
            findNavController().navigate(action)


        }
    }


}



