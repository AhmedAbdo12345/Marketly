package iti.mad.marketly.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.databinding.FragmentAddressBinding
import iti.mad.marketly.utils.AlertManager


class Address : Fragment(){
lateinit var binding: FragmentAddressBinding
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayToolBar()
        var arrayCity = resources.getStringArray(R.array.city)
        binding.autoCompleteTxtViewCity.hint = "Choose Your City"
        val arrayCityDrop_menu = ArrayAdapter<String>(requireContext(), R.layout.dropdown_city, arrayCity)
        binding.autoCompleteTxtViewCity.setAdapter(arrayCityDrop_menu)

        binding.saveAddress.setOnClickListener(View.OnClickListener {
            val address=Address(
                AddressID = binding.autoCompleteTxtViewCountry.text.toString()+binding.autoCompleteTxtViewCity.text.toString(),
                Country = binding.autoCompleteTxtViewCountry.text.toString(),
               City = binding.autoCompleteTxtViewCity.text.toString(),
                Street = binding.editTextStreet.text.toString()
            )

            settingsViewModel.setAddresses(address)
            AlertManager.nonFunctionalDialog("Address Saved",requireContext(),"The Address has been saved successfully")
            findNavController().navigateUp()
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


    private fun displayToolBar() {
        val toolbar = binding.topBarLayout

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar.toolbar)
        activity.supportActionBar?.title = "Address"
        toolbar.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}