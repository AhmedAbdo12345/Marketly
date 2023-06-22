package iti.workshop.admin.presentation.features.auth.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.remote.firestore.FireStoreManager
import iti.workshop.admin.databinding.AuthFragmentEmpolyersListBinding
import iti.workshop.admin.databinding.AuthFragmentLoginBinding
import iti.workshop.admin.databinding.AuthFragmentProfileAddEditBinding
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.model.JopTitle
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.features.auth.viewModel.AuthViewModel
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.loadingDialog
import kotlinx.coroutines.launch

private const val TAG = "AuthProfileAddEditFragm"

@AndroidEntryPoint
class AuthProfileAddEditFragment : Fragment() {

    val viewModel: AuthViewModel by viewModels()

    var isSignUp:Boolean = false
    var actionType: Action = Action.Add
    var user: User = User(name = "User", email = "unKnown")

    lateinit var progressDialog: ProgressDialog
    lateinit var binding: AuthFragmentProfileAddEditBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_profile_add_edit, container, false)
        progressDialog = requireContext().loadingDialog("Checking Add Employer","Please wait ..")

        lifecycleScope.launch {
            viewModel.actionResponse.collect{ state ->
                if (::progressDialog.isInitialized && progressDialog.isShowing)  progressDialog.dismiss()

                state.first?.let {
                    Message.snakeMessage(
                        requireContext(),
                        binding.root,
                        state.second,
                        it
                    )?.show()
                }
            }
        }


        binding.signUpBtn.setOnClickListener {
            if (isUserValid()){
                val employer = User(
                    name = binding.nameInput.text.toString(),
                    email = binding.emailInput.text.toString(),
                    phone = binding.phoneInput.text.toString(),
                    password = binding.passwordInput.text.toString(),
                    jopTitle = if (isSignUp) JopTitle.Admin else JopTitle.valueOf(binding.jopTitleInput.text.toString())
                )
                if (::progressDialog.isInitialized && !progressDialog.isShowing)  progressDialog.show()
                viewModel.addUser(requireActivity(),employer)

            }
        }
        updateUserData()
        return binding.root
    }

    private fun updateUserData() {
        val bundle = arguments
        if (bundle != null) {
            actionType = bundle.getSerializable(ConstantsKeys.ACTION_KEY) as Action
            isSignUp = bundle.getBoolean("signUp",false)
            if (actionType == Action.Edit){
                user = bundle.getSerializable(ConstantsKeys.USER_KEY) as User
                binding.model = user
            }

            if (isSignUp){
                binding.jopTitleInput.visibility = View.GONE
            }
        }
    }

    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {

            binding.emailInput.text.isNullOrEmpty() -> binding.emailInput.error = getString(R.string.required)

            !isValidEmail(binding.emailInput.text.toString()) -> binding.emailInput.error =
                getString(R.string.invalid_email)

            binding.passwordInput.text.isNullOrEmpty() -> binding.passwordInput.error =
                getString(R.string.required)

            binding.passwordInput.text.length < 8 -> binding.passwordInput.error =
                getString(R.string.password_length_error)

            else -> isUserDataValid = true
        }
        return isUserDataValid
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}