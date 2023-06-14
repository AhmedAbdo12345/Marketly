package iti.workshop.admin.presentation.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.databinding.AuthFragmentEmpolyersListBinding
import iti.workshop.admin.databinding.AuthFragmentLoginBinding
import iti.workshop.admin.databinding.AuthFragmentProfileAddEditBinding
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.model.User

class AuthProfileAddEditFragment : Fragment() {

    var actionType: Action = Action.Add
    var user: User = User(name = "User", email = "unKnown")

    lateinit var binding: AuthFragmentProfileAddEditBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_profile_add_edit, container, false)

        binding.editAction.setOnClickListener {
            Toast.makeText(requireContext(), "Saved Successfuly", Toast.LENGTH_SHORT).show()
        }
        updateUserData()
        return binding.root
    }

    private fun updateUserData() {
        val bundle = arguments
        if (bundle != null) {
            actionType = bundle.getSerializable(ConstantsKeys.ACTION_KEY) as Action
            if (actionType == Action.Edit){
                user = bundle.getSerializable(ConstantsKeys.USER_KEY) as User
                binding.model = user
            }

        }
    }
}