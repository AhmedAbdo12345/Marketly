package iti.mad.marketly.presentation.draftorder

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.model.draftorder.AppliedDiscount
import iti.mad.marketly.databinding.FragmentAddressListBinding
import iti.mad.marketly.databinding.FragmentDraftAddressBinding
import iti.mad.marketly.presentation.settings.AddressAdapter
import iti.mad.marketly.presentation.settings.AddressListFragmentDirections
import iti.mad.marketly.presentation.settings.SettingsViewModel
import iti.mad.marketly.utils.AdsManager
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.DraftOrderManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DraftAddressFragment : Fragment(),DraftAddressInterface{
    lateinit var binding: FragmentDraftAddressBinding
    lateinit var adapters: DraftAddressAdapter
    var addresses:MutableList<iti.mad.marketly.data.model.settings.Address> = mutableListOf()
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.backArrow.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })
        adapters= DraftAddressAdapter(requireContext(),this)
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
        binding = FragmentDraftAddressBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onAddressSelected() {
        val method = {
            DraftOrderManager.setDiscount(
                AppliedDiscount(
                    AdsManager.value.toString(),
                    AdsManager.clipBoardCode.code,
                    AdsManager.clipBoardCode.code,
                    AdsManager.value.toString())

            )
            var action= DraftAddressFragmentDirections.actionDraftAddressFragmentToPaymentMethodFragment()
            findNavController().navigate(action)
        }
        val cancelMethod = {
            var action= DraftAddressFragmentDirections.actionDraftAddressFragmentToPaymentMethodFragment()
            findNavController().navigate(action)
        }
        AlertManager.functionalDialog("Appling the coupon",requireContext(),"Do you want to apply the coupone you selected?",method,cancelMethod).show()

    }


}