package iti.mad.marketly.presentation.settings

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentAddressListBinding
import iti.mad.marketly.databinding.FragmentSettingsBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.Constants
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddressListFragment : Fragment(),AddressFragmentInterface  {
lateinit var binding:FragmentAddressListBinding
lateinit var adapters: AddressAdapter
var addresses:MutableList<iti.mad.marketly.data.model.settings.Address> = mutableListOf()
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapters= AddressAdapter(requireContext(),this)
        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            var action= AddressListFragmentDirections.actionAddressListFragmentToAddress()
            findNavController().navigate(action)
        })
        settingsViewModel.getAddresses()
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){

            repeatOnLifecycle(Lifecycle.State.STARTED){
                settingsViewModel._addressResponse.collect{
                    when(it){
                        is ResponseState.OnLoading->{
                            binding.addressProgressbar.visibility = View.VISIBLE
                            binding.addressProductRecView.visibility = View.GONE
                        }
                        is ResponseState.OnSuccess->{
                            binding.addressProgressbar.visibility = View.GONE
                            binding.addressProductRecView.visibility = View.VISIBLE
                            addresses= it.response.toMutableList()
                            adapters.submitList(it.response)
                            binding.addressProductRecView.apply {
                                adapter = adapters
                                layoutManager= LinearLayoutManager(context).apply {
                                    orientation= RecyclerView.VERTICAL
                                }
                            }

                        }
                        is ResponseState.OnError->{
                            binding.addressProgressbar.visibility = View.GONE
                            binding.addressProductRecView.visibility = View.GONE
                            Log.i(ContentValues.TAG, "onViewCreated:${it.message} ")
                        }
                        else ->{}
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddressListBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onDelete(address: Address) {
        val routine= {
            settingsViewModel.deleteAddress(address)
            addresses.remove(address)
            adapters.submitList(addresses)

        }

        AlertManager.functionalDialog("Deleting Address",requireContext(),"Are you sure you want to delete this?",
        routine).show()
    }

    override fun onSetAsDefault(address: Address) {
        val routine = {
            val defaultAddress= address.Country+Constants.Address_DELEMETER+address.City+Constants.Address_DELEMETER+address.Street
            SharedPreferenceManager.saveDefaultAddress(defaultAddress,requireContext())
            SettingsManager.addressSetter(defaultAddress)
        }
        AlertManager.functionalDialog("Saving Address As Default",requireContext(),"Do you want to save this address?",routine)
            .show()
    }


}