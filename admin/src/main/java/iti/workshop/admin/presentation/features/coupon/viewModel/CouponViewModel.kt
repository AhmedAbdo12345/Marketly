package iti.workshop.admin.presentation.features.coupon.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.repository.ICouponRepository
import javax.inject.Inject

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val _repo:ICouponRepository
):ViewModel() {
}