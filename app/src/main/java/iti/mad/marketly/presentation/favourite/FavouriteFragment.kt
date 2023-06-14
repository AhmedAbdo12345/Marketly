package iti.mad.marketly.presentation.favourite

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.R
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentFavouriteBinding
import iti.mad.marketly.presentation.MainActivity
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel
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
                            showFavorites(it.response)
                        }

                        is ResponseState.OnLoading -> {
                            //todo
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
        favoriteViewModel.getAllFavourite(SharedPreferenceManager.getFirebaseUID(requireContext()) ?: "")
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.appBarHome.toolbar)
        binding.appBarHome.toolbar.title = getString(R.string.favourite)
        binding.appBarHome.backArrow.setOnClickListener {
            navController.navigateUp()
        }



    }

    private fun showFavorites(products: List<Product>) {
        binding.notLoginConstraint.visibility = View.GONE
        favAdapter = FavoriteAdapter(requireContext())
        gridManager = GridLayoutManager(requireContext(), 2)
        binding.favRecycler.adapter = favAdapter
        binding.favRecycler.layoutManager = gridManager
        favAdapter.submitList(products)

    }

    fun handleRegister() {
        binding.loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.registerBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }


}



