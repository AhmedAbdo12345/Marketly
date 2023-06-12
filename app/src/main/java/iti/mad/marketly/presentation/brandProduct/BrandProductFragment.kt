package iti.mad.marketly.presentation.brandProduct

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.repository.brandproduct.BrandProductRepoImp
import iti.mad.marketly.data.repository.productRepository.ProductRepoImpl
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.databinding.FragmentBrandProductBinding
import kotlinx.coroutines.launch


class BrandProductFragment : Fragment(), BrandProductAdapter.ListItemClickListener {
    lateinit var brandProductViewModel: BrandProductViewModel
    lateinit var binding: FragmentBrandProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandProductViewModel = ViewModelProvider(this).get(BrandProductViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_brand_product, container, false)
        binding = FragmentBrandProductBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var smartCollection =
            BrandProductFragmentArgs.fromBundle(requireArguments()).smartCollection


        var api = RetrofitInstance.api
        var repo = ProductRepoImpl(api)
        brandProductViewModel.getAllBrandProduct(repo, (smartCollection.id).toString())
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                brandProductViewModel._brandProduct.collect{
                    when(it){
                        is  ResponseState.OnLoading ->{

                        }

                        is ResponseState.OnSuccess -> {
                            var brandAdapter = BrandProductAdapter(this@BrandProductFragment)
                            brandAdapter.submitList(it.response.products)
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
            BrandProductFragmentDirections.actionBrandProductFragmentToProductDetailsFragment(
                product.id
            )
        findNavController().navigate(action)
    }




}