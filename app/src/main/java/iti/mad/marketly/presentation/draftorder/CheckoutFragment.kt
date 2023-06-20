package iti.mad.marketly.presentation.draftorder

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoiceX
import iti.mad.marketly.data.model.draftorder.DraftOrderRequest
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentCheckoutBinding
import iti.mad.marketly.databinding.FragmentDraftAddressBinding
import iti.mad.marketly.presentation.settings.SettingsViewModel
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.Constants
import iti.mad.marketly.utils.DraftOrderManager
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CheckoutFragment : Fragment() {
    lateinit var binding: FragmentCheckoutBinding
    private val draftOrderViewModel by viewModels<DraftOrderViewModel> {
        DraftOrderViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val draftOrderLineItemList = DraftOrderManager.getLineList()
        val draftOrderAddress = DraftOrderManager.getShippingAddress()
        val draftOrderCustomer = DraftOrderManager.getCustomer()
        val draftAppliedDiscount = DraftOrderManager.getDiscount()
        var currency:String =SharedPreferenceManager.getSavedCurrency(requireContext()).toString()
        var totalAmount = 0.0
        val invoice = DraftOrderInvoiceX(Constants.EMAIL_BODY,Constants.EMAIL_SUBJECT,SharedPreferenceManager.getUserMAil(requireContext()).toString())
        for (item in draftOrderLineItemList){
            totalAmount =+ item.price.toDouble()
        }
        if(currency == null||currency == ""){
            currency = "USD"
        }
       val draftOrder= DraftOrderManager.buildDraftOrder(draftOrderLineItemList,draftOrderCustomer,draftOrderAddress,currency,draftAppliedDiscount)
        draftOrderViewModel.createDraftOrder(DraftOrderRequest(draftOrder))
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                draftOrderViewModel._draftResponse.collect{
                    when(it){
                        is ResponseState.OnLoading->{

                        }
                        is ResponseState.OnSuccess->{
                           DraftOrderManager.setDraftOrderID(it.response.draft_order.id)
                            Toast.makeText(requireContext(),"Order Created Successfully",Toast.LENGTH_LONG).show()
                        }
                        is ResponseState.OnError->{
                            Log.i(ContentValues.TAG, "onViewCreated:${it.message} ")
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        }
                        else ->{}
                    }
                }
            }
        }
        binding.countryName.text = draftOrderAddress.country
        binding.city.text = draftOrderAddress.city
        binding.discountText.text = draftAppliedDiscount.title
        binding.discountValueFromApi.text = draftAppliedDiscount.value
        binding.totalAmountTextView.text = totalAmount.toString()
        val method = {
                draftOrderViewModel.sendInvoice(DraftOrderInvoice(invoice),DraftOrderManager.getDraftOrderID().toString())
                draftOrderViewModel.completeOrder(DraftOrderManager.getDraftOrderID().toString())


        }
        binding.placeOrder.setOnClickListener(View.OnClickListener {
            AlertManager.functionalDialog("Order Confermation",requireContext(),"Do you want to checkout?",method).show()

        })
    }

}