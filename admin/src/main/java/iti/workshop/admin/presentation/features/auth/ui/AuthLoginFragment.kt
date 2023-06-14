package iti.workshop.admin.presentation.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import iti.workshop.admin.R
import iti.workshop.admin.databinding.AuthFragmentLoginBinding

class AuthLoginFragment : Fragment() {

    lateinit var binding: AuthFragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_login, container, false)

        return binding.root
    }
}