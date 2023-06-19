package iti.workshop.admin.presentation.features.auth.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import iti.workshop.admin.R
import iti.workshop.admin.data.local.shared.SharedManager
import iti.workshop.admin.databinding.AuthFragmentLoginBinding
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.utils.loadingDialog

private const val TAG = "AuthLoginFragment"
class AuthLoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedManager: SharedManager

    lateinit var binding: AuthFragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_login, container, false)
        sharedManager = SharedManager.getInstance(requireContext())
        auth = FirebaseAuth.getInstance()
        val progressDialog: ProgressDialog = requireContext().loadingDialog("Checking Authentication","Please wait until checking if email and Password is Valid")
        binding.loginBtn.setOnClickListener {

            if (isUserValid()){
                progressDialog.show()
                auth.signInWithEmailAndPassword(binding.emailEt.text.toString(), binding.PasswordET.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            val userName:String = if (user?.displayName!=null) user.displayName?: "Admin" else (user?.email?:"Unknown@mail.com").split("@")[0]
                            progressDialog.dismiss()
                            sharedManager.saveUser(User(user?.uid,userName,user?.email))
                            findNavController().navigate(R.id.action_authLoginFragment_to_homeFragment)
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            progressDialog.dismiss()
                            showErrorDialog()
                        }
                    }
            }
        }
        return binding.root
    }



    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {

            binding.emailEt.text.isNullOrEmpty() -> binding.emailEt.error = getString(R.string.required)

            !isValidEmail(binding.emailEt.text.toString()) -> binding.emailEt.error =
                getString(R.string.invalid_email)

            binding.PasswordET.text.isNullOrEmpty() -> binding.passwordErrorTV.error =
                getString(R.string.required)

            binding.PasswordET.text.length < 8 -> binding.passwordErrorTV.error =
                getString(R.string.password_length_error)

            else -> isUserDataValid = true
        }
        return isUserDataValid
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.error))
            .setMessage(getString(R.string.email_or_password_error))
            .setPositiveButton(R.string.ok) { _, _ -> }.setIcon(R.drawable.ic_baseline_clear_24)
            .show()
    }



}