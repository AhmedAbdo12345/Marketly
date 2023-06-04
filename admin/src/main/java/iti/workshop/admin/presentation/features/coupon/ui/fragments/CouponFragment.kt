package iti.workshop.admin.presentation.features.coupon.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.repository.ICouponRepository
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel

@AndroidEntryPoint
class CouponFragment : Fragment() {
    private val viewModel: CouponViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_coupon, container, false)
    }


}