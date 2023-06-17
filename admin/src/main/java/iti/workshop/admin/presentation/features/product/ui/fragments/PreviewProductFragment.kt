package iti.workshop.admin.presentation.features.product.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.ProductFragmentPreviewProductBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel


@AndroidEntryPoint
class PreviewProductFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    lateinit var binding:ProductFragmentPreviewProductBinding

    lateinit var product:Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.product_fragment_preview_product,container,false)
        binding.lifecycleOwner = viewLifecycleOwner


        updateProduct()
        navigateToEditProduct()
        return binding.root
    }

    private fun updateProduct() {
        val bundle = arguments
        if (bundle != null) {
            product = bundle.getSerializable(ConstantsKeys.PRODUCT_KEY) as Product
            binding.dataModel = product
        }
    }

    private fun navigateToEditProduct() {
        binding.saveActionBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ConstantsKeys.PRODUCT_KEY, product)
            bundle.putSerializable(ConstantsKeys.ACTION_KEY, Action.Edit)
            findNavController().navigate(R.id.action_previewProductFragment_to_addAndEditProductFragment,bundle)

        }
    }

}