package iti.workshop.admin.presentation.features.product.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.databinding.ProductDialogImagePreviewBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class ImagePreviewDialog: DialogFragment() {

    private var imageModel:Image?=null
    lateinit var binding: ProductDialogImagePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.product_dialog_image_preview,
            container,
            false
        )
        val bundle = arguments
        if (bundle != null) {
            imageModel = bundle.getSerializable(ConstantsKeys.IMAGE_KEY) as Image
            binding.model = imageModel
            binding.imageTitle.text = "this is test"
   }

        binding.closeAction.setOnClickListener {
            dismiss()
        }

        return binding.root
    }







}