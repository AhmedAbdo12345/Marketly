package iti.workshop.admin.presentation.features.coupon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import iti.workshop.admin.R
import iti.workshop.admin.databinding.CouponDiscountCodeFragmentBinding

class DiscountCodeFragment : Fragment() {

    lateinit var binding: CouponDiscountCodeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.coupon_discount_code_fragment, container, false)

        return binding.root
    }
}