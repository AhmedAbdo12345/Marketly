package iti.workshop.admin.presentation.features.coupon.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.databinding.CouponFragmentBinding
import iti.workshop.admin.presentation.features.coupon.adapters.TabsPagerAdapter
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel

@AndroidEntryPoint
class CouponFragment : Fragment() {

    lateinit var binding:CouponFragmentBinding
    val viewModel: CouponViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.coupon_fragment,container,false)
        binding.tabs.setupWithViewPager(binding.viewpager)
        val adapter = TabsPagerAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(DiscountCodeFragment(),"Discount Code")
        adapter.addFragment(PriceRuleFragment(),"Price Rule")
        binding.viewpager.adapter = adapter

        return binding.root
    }


}