package iti.mad.marketly.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.databinding.FragmentAddressBinding
import iti.mad.marketly.databinding.FragmentSettingsBinding
import iti.mad.marketly.utils.Constants
import iti.mad.marketly.utils.SettingsManager


class Address : Fragment() {
lateinit var binding: FragmentAddressBinding
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveAddress.setOnClickListener(View.OnClickListener {
            val address=Address(
                AddressID = binding.country.text.toString()+binding.city.text.toString(),
                Country = binding.country.text.toString(),
                City = binding.city.text.toString(),
                Street = binding.street.text.toString()
            )

            settingsViewModel.setAddresses(address)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }


}