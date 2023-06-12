package iti.workshop.admin.presentation.features.coupon.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.databinding.CouponDialogAddDiscountCodeBinding
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel
import kotlinx.coroutines.launch

class AddDiscountCodeDialog(
    private val viewModel:CouponViewModel,
    private val priceRuleId:Long = -1L
) : DialogFragment() {

    lateinit var binding: CouponDialogAddDiscountCodeBinding

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.coupon_dialog_add_discount_code,
            null,
            false
        )

        binding.addAction.setOnClickListener {
            if (isValidData()){
                saveData()
            }
        }
//        dialog?.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))



        lifecycleScope.launch {
            viewModel.discountCodeActionResponse.collect{ state ->
                state.first?.let {
                    if (it) {
                        dismiss()
                    }
                }
            }
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        return dialog
    }



    private fun saveData() {
        if (priceRuleId == -1L) {
            Toast.makeText(requireContext(), "There is an unexpected error please try again", Toast.LENGTH_SHORT).show()
            return
        }

        val model = DiscountCode(
            code = binding.titleInput.text.toString(),
            price_rule_id = priceRuleId,
            usage_count = binding.valueInput.text.toString().toInt()
        )

        viewModel.saveDiscountCode(model)
    }

    private fun isValidData(): Boolean {
       if( binding.titleInput.text.isNullOrBlank()){
           binding.titleInput.error = "Please put name of code"
           return  false
       }

        if (binding.valueInput.text.isNullOrBlank()){
            binding.valueInput.error = "Please put usage count"
            return  false
        }

        return true
    }



}