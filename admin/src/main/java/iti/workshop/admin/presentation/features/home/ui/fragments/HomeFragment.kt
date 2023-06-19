package iti.workshop.admin.presentation.features.home.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.shared.SharedManager
import iti.workshop.admin.databinding.HomeFragmentBinding
import iti.workshop.admin.databinding.ProductFragmentPreviewProductBinding
import iti.workshop.admin.presentation.features.home.model.HomeModel
import iti.workshop.admin.presentation.features.home.viewModel.HomeViewModel
import iti.workshop.admin.presentation.utils.DataResponseState
import iti.workshop.admin.presentation.utils.alert
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var sharedManager:SharedManager
    lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        sharedManager =  SharedManager.getInstance(requireContext())

        navigationSelect();updateUI()

        binding.userModel = sharedManager.getUser()
        binding.logoutBtn.setOnClickListener {
            requireContext().alert("Logging out","Do you want logout? \n Are you sure?",{
                     if (sharedManager?.clearUser() == true) requireActivity().finish()
            },{}
            )
        }


        return binding.root
    }

    private fun updateUI() {
        lifecycleScope.launch {
            viewModel.counts.collect{
                when(it){
                    is DataResponseState.OnError -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataResponseState.OnLoading -> {
                        binding.model = HomeModel()
                    }
                    is DataResponseState.OnSuccess -> {
                        binding.model = it.data
                    }
                }
            }
        }
    }

    private fun navigationSelect() {
        binding.adminTitle.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_authProfilePreviewFragment) }
        binding.adminImage.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_authProfilePreviewFragment) }
        binding.cardsHolder.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_productsListFragment) }
        binding.inventoryHolder.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_inventoryFragment) }
        binding.couponHolder.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_priceRuleFragment) }
    }

}