package iti.mad.marketly.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.R
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.databinding.FragmentLoginBinding
import iti.mad.marketly.presentation.setCustomFocusChangeListener
import iti.mad.marketly.presentation.MainActivity
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: FragmentLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var backButton:  ImageButton

    private lateinit var errorTextView: TextView
    private lateinit var forgetPasswordButton: TextView

    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                loginViewModel.customerRespoonse.collect { uiState ->

                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        is ResponseState.OnLoading -> {
                            //todo

                        }

                        is ResponseState.OnError -> {
                            showErrorDialog()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = binding.emailEt.apply {
            setCustomFocusChangeListener()
        }
        passwordEditText = binding.PasswordET.apply {
            setCustomFocusChangeListener()
        }
        forgetPasswordButton= binding.forgotPasswordTv.apply {
            setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
                findNavController().navigate(action)
            }
        }
        loginButton = binding.loginBtn
        errorTextView = binding.emailErrorTV
        backButton = binding.backButton.apply {
            setOnClickListener {
                findNavController().navigateUp()
            }

        }
        loginButton.setOnClickListener {
            if (isUserValid()) {
                firebaseAuth.signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        getUserData(emailEditText.text.toString())
                        SettingsManager.documentIDSetter(emailEditText.text.toString())
                    } else {
                        showErrorDialog()
                    }
                }
            }

        }
    }

    private fun getUserData(email: String) {
        loginViewModel.loginWithEmail(email)

    }

    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {

            emailEditText.text.isNullOrEmpty() -> emailEditText.error =
                getString(R.string.required)

            !isValidEmail(emailEditText.text.toString()) -> emailEditText.error =
                getString(R.string.invalid_email)

            passwordEditText.text.isNullOrEmpty() -> binding.passwordErrorTV.error =
                getString(R.string.required)

            passwordEditText.text.length < 8 -> binding.passwordErrorTV.error =
                getString(R.string.password_length_error)

            else -> isUserDataValid = true
        }
        return isUserDataValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
    fun showErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(getString(R.string.email_or_password_error))
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setIcon(R.drawable.ic_baseline_clear_24)
            .show()
    }
}