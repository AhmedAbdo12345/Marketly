package iti.workshop.admin.presentation.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.AuthFragmentLoginBinding
import iti.workshop.admin.databinding.AuthFragmentProfilePreviewBinding
import iti.workshop.admin.databinding.ProductFragmentPreviewProductBinding
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.features.auth.viewModel.AuthViewModel
import iti.workshop.admin.presentation.features.product.viewModel.ProductViewModel

@AndroidEntryPoint
class AuthProfilePreviewFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    lateinit var user:User
    lateinit var binding: AuthFragmentProfilePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_profile_preview, container, false)

        updateUser()
        navigateToEdit()
        return binding.root
    }

    private fun updateUser() {
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getSerializable(ConstantsKeys.USER_KEY) as User
            binding.model = user
        }
    }
    private fun navigateToEdit() {
        binding.editAction.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ConstantsKeys.USER_KEY, user)
            bundle.putSerializable(ConstantsKeys.ACTION_KEY, Action.Edit)
            findNavController().navigate(R.id.action_authProfilePreviewFragment_to_authProfileAddEditFragment,bundle)
        }
    }
}