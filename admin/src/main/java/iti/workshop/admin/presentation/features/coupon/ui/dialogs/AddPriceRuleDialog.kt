package iti.workshop.admin.presentation.features.coupon.ui.dialogs

import android.annotation.SuppressLint
import android.graphics.drawable.DrawableWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import iti.workshop.admin.R
import iti.workshop.admin.databinding.CouponDialogAddPriceRuleBinding

class AddPriceRuleDialog : DialogFragment() {

    lateinit var binding: CouponDialogAddPriceRuleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.coupon_dialog_add_price_rule,
            container,
            false
        )

        return binding.root
    }
}