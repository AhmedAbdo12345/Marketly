package iti.mad.marketly.presentation.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.data.model.category.CustomCollection
import iti.mad.marketly.databinding.FragmentCategoryBinding
import kotlinx.coroutines.launch


class CategoryFragment : Fragment(), CategoryAdapter.ListItemClickListener {
    lateinit var  viewModel : CategoryViewModel
    lateinit var binding:FragmentCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, CategoryViewModel.Factory).get(CategoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_category, container, false)
        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllCategory()
            viewModel._category.collect{
                when(it){
                    is ResponseState.OnLoading ->{

                    }
                    is ResponseState.OnSuccess ->{
                        val categoryAdapter = CategoryAdapter(this@CategoryFragment)
                         categoryAdapter.submitList(it.response.custom_collections)
                        binding.categoryRecView.apply {
                            adapter = categoryAdapter
                            setHasFixedSize(true)
                            layoutManager = GridLayoutManager(context, 1).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }
                    is ResponseState.OnError ->{
                        Log.d("zxcv", "onViewCreated: 88888888888888")

                    }
                    else -> {}
                }
            }
        }
    }

    override fun onClickCategory(customCollection: CustomCollection) {
        TODO("Not yet implemented")
    }
}