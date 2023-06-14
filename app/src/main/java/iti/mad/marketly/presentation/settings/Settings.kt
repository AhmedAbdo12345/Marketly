package iti.mad.marketly.presentation.settings

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.data.repository.settings.SettingsRepoImplementation
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.databinding.FragmentHomeBinding
import iti.mad.marketly.databinding.FragmentSettingsBinding
import iti.mad.marketly.presentation.home.brands.BrandsViewModel
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class Settings : Fragment() {

    lateinit var binding : FragmentSettingsBinding
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }
    var rate:Double=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEtSettingsPage.isEnabled=false
        val name=binding.nameEtSettingsPage.text.toString()
        binding.nameEtSettingsPage.setText(SettingsManager.getUserName())
        binding.AddressPage.setOnClickListener(View.OnClickListener {
            var action= SettingsDirections.actionSettingsToAddressListFragment()
            findNavController().navigate(action)
        })
        binding.EditName.setOnClickListener(View.OnClickListener {
            binding.nameEtSettingsPage.setText("")
            binding.nameEtSettingsPage.isEnabled=true
        })
        binding.darkModeSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        binding.saveSettingSettingsBtn.setOnClickListener(View.OnClickListener {
            val function={
                SharedPreferenceManager.saveUserName(binding.nameEtSettingsPage.text.toString(),requireContext())
                val selectedOption: Int = binding.money.checkedRadioButtonId
                if(selectedOption==R.id.EGP){
                    SettingsManager.curSetter("EGP")
                    SettingsManager.curSetter("EGP")
                }else{
                    SettingsManager.curSetter("USD")
                    SettingsManager.curSetter("USD")
                }
                getExchangeRate()

            }
            AlertManager.functionalDialog("Save Your Settings",requireContext(),"Do you want to save your settings ?",function)
                .show()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settings, container, false)
        binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }
    fun getExchangeRate(){
        settingsViewModel.getExchangeRate()
        lifecycleScope.launch(Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                settingsViewModel._currency.collect{
                    when(it){
                        is ResponseState.OnLoading->{

                        }
                        is ResponseState.OnSuccess->{
                            rate=it.response.conversion_rates.EGP
                            SettingsManager.exchangeRateSetter(rate)
                        }
                        is ResponseState.OnError->{
                            Log.i(TAG, "onViewCreated:${it.message} ")
                        }
                        else ->{}
                    }
                }
            }
        }
    }
}