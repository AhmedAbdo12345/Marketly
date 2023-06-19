package iti.mad.marketly.presentation.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.R
import iti.mad.marketly.databinding.FragmentForgetPasswordBinding
import iti.mad.marketly.utils.setCustomFocusChangeListener

class ForgetPasswordFragment : Fragment() {

    lateinit var emailEditText: EditText
    lateinit var sendButton: Button
    lateinit var binding: FragmentForgetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = binding.editTextTextEmailAddress.apply {
            setCustomFocusChangeListener()
        }
        sendButton = binding.button.apply {
            setOnClickListener {
                    if (isUserValid()){
                        auth.sendPasswordResetEmail(emailEditText.text.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Password reset email sent.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    findNavController().navigateUp()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to send password reset email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
            }
        }
    }
    private fun isUserValid(): Boolean {

        var isUserDataValid = false
        when {

            emailEditText.text.isNullOrEmpty() -> binding.emailErrorTV.text =
                getString(R.string.required)

            !isValidEmail(emailEditText.text.toString()) -> binding.emailErrorTV.text =
                getString(R.string.invalid_email)

            else -> isUserDataValid = true
        }
        return isUserDataValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}