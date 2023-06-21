package iti.mad.marketly.presentation.draftorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.databinding.FragmentCreditBinding
import iti.mad.marketly.databinding.FragmentPaymentMethodBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.DraftOrderManager


class CreditFragment : Fragment() {
lateinit var binding:FragmentCreditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreditBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if(binding.emailEt.text.toString() == "0"){
                AlertManager.nonFunctionalDialog("Error",requireContext(),"Please enter a valid creditNumber")
            }else{
                DraftOrderManager.setCreidet(binding.emailEt.text.toString().toLong())
                findNavController().navigateUp()
            }
        })
    }
}