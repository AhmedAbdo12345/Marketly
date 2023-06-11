package iti.workshop.admin.presentation.features.coupon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.CouponDiscountCodeFragmentBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.coupon.ui.adapters.DiscountCodeAdapter
import iti.workshop.admin.presentation.features.coupon.ui.adapters.DiscountCodeOnCLickListener
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscountCodeFragment : Fragment() {

    val viewModel:CouponViewModel by viewModels()
    lateinit var binding: CouponDiscountCodeFragmentBinding
    lateinit var adapter:DiscountCodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.coupon_discount_code_fragment, container, false)
        binding.lifecycleOwner = this
        adapter = DiscountCodeAdapter(DiscountCodeOnCLickListener(::selectItem,::deleteItem))
        binding.mAdapter = adapter

        updateDiscountList()
        updateUISate()
        addPriceRuleAction()
        return binding.root
    }

    private fun deleteItem(discountCode: DiscountCode) {
    }
    private fun selectItem(discountCode: DiscountCode) {
    }
    private fun updateDiscountList() {
        val bundle = arguments
        if (bundle != null) {
            val priceRuleId = bundle.getLong(ConstantsKeys.PRICE_RULE_ID_KEY)
            if (priceRuleId == -1L){
                dataViewStates(DataStates.Nothing)
            }else{
                viewModel.retrieveDiscountRules(priceRuleId)
            }
        }
    }
    private fun addPriceRuleAction() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_discountCodeFragment_to_addDiscountCodeDialog)
        }
    }
    private fun updateUISate() {
        lifecycleScope.launch {
            viewModel.discountCodeResponse.collect{state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStates(DataStates.Error)
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                message,
                                false
                            )?.show()
                        }
                    }
                    is DataListResponseState.OnLoading -> {
                        dataViewStates(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStates(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStates(DataStates.Data)
                        adapter.submitList(state.data.discount_codes)

                    }
                }
            }
        }

    }
    private fun dataViewStates(dataStates: DataStates) {
        when(dataStates){
            DataStates.Data -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.VISIBLE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Nothing -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.VISIBLE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Error -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.VISIBLE
            }
            DataStates.Loading ->{
                binding.shimmerResults.visibility = View.VISIBLE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
        }

    }
}