package iti.workshop.admin.presentation.features.coupon.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.PriceRule
import iti.workshop.admin.databinding.CouponDialogAddPriceRuleBinding
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel
import iti.workshop.admin.presentation.utils.TimeConverter
import kotlinx.coroutines.launch

class AddPriceRuleDialog(private val viewModel:CouponViewModel) : DialogFragment() {

    private lateinit var datePickerTo: MaterialDatePicker<Long?>
    var dateFrom: String? = null;
    var dateTo: String? = null
    lateinit var binding: CouponDialogAddPriceRuleBinding


    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.coupon_dialog_add_price_rule,
            null,
            false
        )

        binding.addAction.setOnClickListener {
            if (isValidData()){
                saveData()
            }
        }

        binding.addAction.setOnClickListener {
            if (isValidData()) {
                saveData()
            }
        }

        val constraintBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(System.currentTimeMillis())).build()
        val datePickerFrom =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date From")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintBuilder)
                .build()

        binding.dateFromInput.setOnClickListener {
            datePickerFrom.show(requireActivity().supportFragmentManager, "DummyTag")
        }

        binding.dateToInput.setOnClickListener {
            if (::datePickerTo.isInitialized) {
                datePickerTo.show(requireActivity().supportFragmentManager, "DummyTag")
                datePickerTo.addOnPositiveButtonClickListener { dateLong ->
                    dateLong?.let {
                        dateTo = TimeConverter.convertTimestampToString(
                            it,
                            TimeConverter.DATE_TIME_GLOBAL_PATTERN
                        )
                        binding.dateToInput.text =
                            TimeConverter.convertTimestampToString(it, TimeConverter.DATE_PATTERN)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please select start first", Toast.LENGTH_SHORT)
                    .show()
            }

        }


        datePickerFrom.addOnPositiveButtonClickListener { dateLong ->
            dateLong?.let {
                dateFrom = TimeConverter.convertTimestampToString(
                    it,
                    TimeConverter.DATE_TIME_GLOBAL_PATTERN
                )
                binding.dateFromInput.text =
                    TimeConverter.convertTimestampToString(it, TimeConverter.DATE_PATTERN)

                updateDatePickerFrom(it)

            }
        }

        lifecycleScope.launch {
            viewModel.priceRuleActionResponse.collect{ state ->
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

    private fun updateDatePickerFrom(dateTimeStamp: Long) {
        val constraintBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(dateTimeStamp)).build()
        datePickerTo =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date To")
                .setCalendarConstraints(constraintBuilder).build()
    }

    private fun saveData() {

        val model = PriceRule(
            title = binding.titleInput.text.toString(),
            value = (binding.valueInput.text.toString().toInt()*-1).toString(),
            starts_at = dateFrom,
            ends_at = dateTo

        )

        viewModel.savePriceRule(model)

    }

    private fun isValidData(): Boolean {
        if (binding.titleInput.text.isNullOrBlank()) {
            binding.titleInput.error = "Please put name of code"
            return false
        }

        if (binding.valueInput.text.isNullOrBlank()) {
            binding.valueInput.error = "Please put usage count"
            return false
        }

        if (binding.dateFromInput.text.isNullOrBlank()) {
            binding.dateFromInput.error = "Please put date from"
            return false
        }

        if (binding.dateToInput.text.isNullOrBlank()) {
            binding.dateToInput.error = "Please put date from"
            return false
        }


        return true
    }

}