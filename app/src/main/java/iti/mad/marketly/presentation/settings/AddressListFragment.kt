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
import iti.mad.marketly.databinding.FragmentAddressListBinding
import iti.mad.marketly.databinding.FragmentSettingsBinding
import iti.mad.marketly.utils.ResponseState
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AddressListFragment : Fragment() {
lateinit var binding:FragmentAddressListBinding
lateinit var adapters: AddressAdapter
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapters= AddressAdapter(requireContext())
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
                            Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                        }
                        is ResponseState.OnSuccess->{
                            Toast.makeText(requireContext(),"${it.response.get(0).City}",Toast.LENGTH_LONG).show()
                            adapters.submitList(it.response)
                            binding.categoryProductRecView.apply {
                                adapter = adapters
                                layoutManager= LinearLayoutManager(context).apply {
                                    orientation= RecyclerView.VERTICAL
                                }
                            }

                        }
                        is ResponseState.OnError->{
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


}