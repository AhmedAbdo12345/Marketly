package iti.mad.marketly.presentation.auth.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.R
import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentRegisterBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.setCustomFocusChangeListener
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var alreadyHaveAccountTextView: TextView
    private lateinit var backButton: ImageButton
    lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        RegisterViewModel.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.customerRespoonse.collect { uiState ->
                    when (uiState) {
                        is ResponseState.OnSuccess -> {
                            SharedPreferenceManager.saveUserName(
                                nameEditText.text.toString(),
                                requireContext()
                            )
                            binding.progressBar.visibility = View.GONE
                            AlertManager.functionalDialog(
                                "Register Successfully",
                                requireContext(),
                                "please check your E-mail for verification"
                            ,{
                                    val action =
                                        RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                                    findNavController().navigate(action)
                                }).show()
                        }

                        is ResponseState.OnLoading -> {

                            if (uiState.loading) {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                        }

                        is ResponseState.OnError -> {
                            binding.progressBar.visibility = View.GONE

                            AlertManager.nonFunctionalDialog(
                                "errrrrrrrrrrrrrrror",
                                requireContext(),
                                uiState.message,

                                )

                        }
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
                        binding.textViewEmailError.text = getString(R.string.required)
                    } else if (!isValidEmail(email)) {
                        binding.textViewEmailError.text = getString(R.string.invalid_email)
                    } else {
                        binding.textViewEmailError.text = null
                    }
                }
            })

        }
        nameEditText = binding.nameEt.apply {
            setCustomFocusChangeListener()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int, count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    val name = s.toString().trim { it <= ' ' }
                    if (name.isEmpty()) {
                        binding.textViewNameError.text = getString(R.string.required)
                    } else {
                        binding.textViewNameError.text = null
                    }
                }
            })
        }
        alreadyHaveAccountTextView = binding.alreadyHaveAccTV.apply {
            setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
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
                        binding.textViewPasswordError.text = "Password is required"
                    } else if (password.length < 8) {
                        binding.textViewPasswordError.text =
                            "Password must be at least 8 characters"
                    } else {
                        binding.textViewPasswordError.text = null
                    }
                }
            })

        }
        confirmPasswordEditText = binding.confirmPasswordET.apply {
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
                        binding.textViewPasswordError.text = getString(R.string.required)
                    } else if (password.length < 8) {
                        binding.textViewPasswordError.text =
                            getString(R.string.password_length_error)
                    } else if (password != binding.confirmPasswordET.text.toString()) {
                        binding.textViewConfirmPasswordError.text =
                            getString(R.string.confirm_password_error)
                    } else {
                        binding.textViewConfirmPasswordError.text = null
                    }
                }
            })

        }
        registerButton = binding.signUpBtn/* backButton.setOnClickListener {

         }*/

        registerButton.setOnClickListener {

            if (isUserValid()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(), passwordEditText.text.toString()
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                        val customer = Customer()
                        customer.email = emailEditText.text.toString()
                        customer.last_name = passwordEditText.text.toString()
                        customer.first_name = nameEditText.text.toString()
                        val customerBody = CustomerBody(customer)
                        registerViewModel.registerUser(customerBody)
                    } else {
                        AlertManager.nonFunctionalDialog(
                            "error",
                            requireContext(),
                            task.exception?.message!!
                        )
                    }
                }

            }

        }

    }

    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {
            nameEditText.text.isNullOrEmpty() -> binding.textViewNameError.text =
                getString(R.string.required)

            emailEditText.text.isNullOrEmpty() -> binding.textViewEmailError.text =
                getString(R.string.required)

            !isValidEmail(emailEditText.text.toString()) -> binding.textViewEmailError.text =
                getString(R.string.invalid_email)

            passwordEditText.text.isNullOrEmpty() -> binding.textViewPasswordError.text =
                getString(R.string.required)

            confirmPasswordEditText.text.isNullOrEmpty() -> binding.textViewConfirmPasswordError.text =
                getString(R.string.required)

            passwordEditText.text.length < 8 -> binding.textViewPasswordError.text =
                getString(R.string.password_length_error)

            confirmPasswordEditText.text.toString() != passwordEditText.text.toString() -> binding.textViewConfirmPasswordError.text =
                getString(R.string.confirm_password_error)

            else -> isUserDataValid = true
        }
        return isUserDataValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }


}