package iti.workshop.admin.presentation.features.product.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductDialogAddVariantBinding
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class AddVariantToServerDialog(
    private val viewModel:ProductViewModel,
    private val productId:Long = -1L
) : DialogFragment() {

    lateinit var binding: ProductDialogAddVariantBinding

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.product_dialog_add_variant,
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
            viewModel.actionResponse.collect{ state ->
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
        if (productId == -1L) {
            Toast.makeText(requireContext(), "There is an unexpected error please try again", Toast.LENGTH_SHORT).show()
            return
        }

        val model = Variant(
            title = binding.titleInput.text.toString(),
            price = binding.valueInput.text.toString(),
            option1 = binding.sizeInput.text.toString(),
            option2 = binding.colorInput.text.toString()

        )

        viewModel.addVariant(productId,model)
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

        if (binding.valueInput.text.toString().toFloat() == 0f) {
            binding.valueInput.error = "Please put value of product more than Zero"
            return false
        }

        if (binding.valueInput.text.toString().toFloat() > 10000f) {
            binding.valueInput.error = "Please don't put value of product more than 10,000"
            return false
        }

        if( binding.colorInput.text.isNullOrBlank()){
           binding.colorInput.error = "Please put name of color"
           return  false
       }

        if (binding.sizeInput.text.isNullOrBlank()){
            binding.sizeInput.error = "Please put size of item\n or put zero by default if nothing"
            return  false
        }



        return true
    }



}