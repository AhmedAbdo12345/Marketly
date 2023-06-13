package iti.mad.marketly.presentation.brandProduct

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.databinding.FragmentBrandProductBinding
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.launch


class BrandProductFragment : Fragment(), BrandProductAdapter.ListItemClickListener {
    val brandProductViewModel: BrandProductViewModel by viewModels<BrandProductViewModel> {
        BrandProductViewModel.Factory
    }
    lateinit var binding: FragmentBrandProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_brand_product, container, false)
        binding = FragmentBrandProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var smartCollection =
            BrandProductFragmentArgs.fromBundle(requireArguments()).brandID
        brandProductViewModel.getAllBrandProduct(
            smartCollection.toString(), FirebaseAuth.getInstance().currentUser?.uid.toString()
        )
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                brandProductViewModel._brandProduct.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnLoading -> {

                        }

                        is ResponseState.OnSuccess -> {
                            var brandAdapter = BrandProductAdapter(this@BrandProductFragment) {
                                if (it.isFavourite == true) {
                                    brandProductViewModel.deleteProductFromFavourite(
                                        FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                        it
                                    )

                                } else {
                                    brandProductViewModel.addProductToFavourite(
                                        FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                        it
                                    )
                                }
                            }
                            brandAdapter.submitList(uiState.response)
                            binding.brandProductRecycleView.apply {
                                adapter = brandAdapter
                                setHasFixedSize(true)
                                layoutManager = GridLayoutManager(context, 2).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }
                        }

                        is ResponseState.OnError -> {
                            Log.d("zxcv", "onViewCreated: 88888888888888")

                        }

                        else -> {}
                    }
                }
            }
        }

    }

    override fun onClickProduct(product: Product) {
        val action =
            product.id?.let {
                BrandProductFragmentDirections.actionBrandProductFragmentToProductDetailsFragment(
                    it
                )
            }
        findNavController().navigate(action!!)
    }


}