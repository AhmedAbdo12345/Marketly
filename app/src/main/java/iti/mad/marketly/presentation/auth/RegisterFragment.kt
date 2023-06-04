package iti.mad.marketly.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.R
import iti.mad.marketly.data.model.Customer
import iti.mad.marketly.data.model.CustomerBody
import iti.mad.marketly.databinding.FragmentLoginBinding
import iti.mad.marketly.databinding.FragmentRegisterBinding
import iti.mad.marketly.presentation.MainActivity
import iti.mad.marketly.presentation.auth.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var errorTextView: TextView
    lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val registerViewModel by viewModels<RegisterViewModel>{
        RegisterViewModel.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = binding.emailEt
        passwordEditText = binding.PasswordET
        confirmPasswordEditText = binding.confirmPasswordET
        registerButton = binding.signUpBtn
        errorTextView = binding.emailErrorTV
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (validUser()) {

                val customer = Customer()
                customer.email = email
                customer.last_name = password
                val customerBody = CustomerBody(customer)
                registerViewModel.registerUser(customerBody)
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        errorTextView.text = task.exception?.message
                    }
                }
        }

    }

    fun validUser(): Boolean {

        var email = binding.emailEt.text

        var password = binding.PasswordET.text
        var confirm = binding.confirmPasswordET.text
        var isUserDataValid = false
        when {
            email.isNullOrEmpty() -> binding.emailEt.error = getString(R.string.required)
            password.isNullOrEmpty() -> binding.passwordErrorTV.error = getString(R.string.required)
            confirm.isNullOrEmpty() -> binding.confirmPasswordET.error =
                getString(R.string.required)

            password.length < 8 -> binding.confirmPasswordET.error =
                getString(R.string.password_length_error)

            confirm.toString() != password.toString() -> binding.confirmPasswordET.error =
                getString(R.string.confirm_password_error)

            else -> isUserDataValid = true
        }
        return isUserDataValid

    }

}