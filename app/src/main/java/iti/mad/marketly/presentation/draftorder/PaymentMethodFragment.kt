package iti.mad.marketly.presentation.draftorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentDraftAddressBinding
import iti.mad.marketly.databinding.FragmentPaymentMethodBinding
import iti.mad.marketly.utils.CurrencyConverter
import iti.mad.marketly.utils.DraftOrderManager
import iti.mad.marketly.utils.SettingsManager


class PaymentMethodFragment : Fragment() {
lateinit var binding : FragmentPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveSettingSettingsBtn.setOnClickListener(View.OnClickListener {
            val selectedOption: Int = binding.money.checkedRadioButtonId
            if(selectedOption==R.id.credit_card){
                DraftOrderManager.gotoCredit()
            }else{
                DraftOrderManager.gotoPay()
            }
            if(DraftOrderManager.getDirection()==0){
                val action = PaymentMethodFragmentDirections.actionPaymentMethodFragmentToCheckoutFragment()
                findNavController().navigate(action)
            }else{
                val action = PaymentMethodFragmentDirections.actionPaymentMethodFragmentToChecoutFragmentCredit()
                findNavController().navigate(action)
            }
        })
    }


}