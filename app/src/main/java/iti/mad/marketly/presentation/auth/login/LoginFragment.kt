package iti.mad.marketly.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentLoginBinding
import iti.mad.marketly.presentation.MainActivity
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import iti.mad.marketly.utils.setCustomFocusChangeListener
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var backButton: ImageButton

    private lateinit var errorTextView: TextView
    private lateinit var forgetPasswordButton: TextView

    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                loginViewModel.customerRespoonse.collect { uiState ->

                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            SharedPreferenceManager.saveCurrency(
                                uiState.response.customers?.get(0)?.currency ?: "", requireContext()
                            )
                            Log.i("IDD", uiState.response.customers?.get(0)?.id.toString())
                            SharedPreferenceManager.saveUserData(
                                requireContext(),
                                uiState.response.customers?.get(0)?.id.toString() ?: "",
                                uiState.response.customers?.get(0)?.email ?: "",
                                uiState.response.customers?.get(0)?.first_name ?: ""
                            )
                            binding.progressBar2.visibility = View.GONE
                            getSavedSettings()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        is ResponseState.OnLoading -> {

                            if (uiState.loading) {
                                binding.progressBar2.visibility = View.VISIBLE
                            }

                        }

                        is ResponseState.OnError -> {
                            binding.progressBar2.visibility = View.GONE
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
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val email = s.toString().trim { it <= ' ' }
                    if (email.isEmpty()) {
                        binding.emailErrorTV.text = getString(R.string.required)
                    } else if (!isValidEmail(email)) {
                        binding.emailErrorTV.text = getString(R.string.invalid_email)
                    } else {
                        binding.emailErrorTV.text = null
                    }
                }
            })
        }
        passwordEditText = binding.PasswordET.apply {
            setCustomFocusChangeListener()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val password = s.toString().trim { it <= ' ' }
                    if (password.isEmpty()) {
                        binding.passwordErrorTV.text = getString(R.string.required)
                    } else if (password.length < 8) {
                        binding.passwordErrorTV.text = getString(R.string.password_length_error)
                    } else {
                        binding.passwordErrorTV.text = null
                    }
                }
            })
        }
        forgetPasswordButton = binding.forgotPasswordTv.apply {
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
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(), passwordEditText.text.toString()
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        if (task.result.user?.isEmailVerified == true) {
                            getUserData(emailEditText.text.toString())
                            SettingsManager.documentIDSetter(emailEditText.text.toString())
                            SharedPreferenceManager.saveFirebaseUID(
                                FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            )
                        } else {
                            AlertManager.nonFunctionalDialog(
                                "email verification",
                                requireContext(),
                                "please Verify your Email before login"
                            )
                        }


                    } else {
                        showErrorDialog()
                    }
                }
            }

        }
    }

    private fun getUserData(email: String) {
        val e = "email:$email"
        loginViewModel.loginWithEmail(e)

    }

    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {

            emailEditText.text.isNullOrEmpty() -> emailEditText.error = getString(R.string.required)

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

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.error))
            .setMessage(getString(R.string.email_or_password_error))
            .setPositiveButton(R.string.ok) { _, _ -> }.setIcon(R.drawable.ic_baseline_clear_24)
            .show()
    }

    private fun getSavedSettings() {
        SettingsManager.documentIDSetter(emailEditText.text.toString())
        SettingsManager.userNameSetter(SharedPreferenceManager.getUserName(requireContext())!!)
        SettingsManager.addressSetter(SharedPreferenceManager.getDefaultAddress(requireContext())!!)
        SettingsManager.curSetter(SharedPreferenceManager.getSavedCurrency(requireContext())!!)
        SettingsManager.exchangeRateSetter(
            SharedPreferenceManager.getDefaultExchangeRate(
                requireContext()
            )!!
        )
    }
}