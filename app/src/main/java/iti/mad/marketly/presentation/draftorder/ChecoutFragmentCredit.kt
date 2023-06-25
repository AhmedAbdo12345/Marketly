package iti.mad.marketly.presentation.draftorder

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
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
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import iti.mad.marketly.R
import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoiceX
import iti.mad.marketly.data.model.draftorder.DraftOrderRequest
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentCheckoutBinding
import iti.mad.marketly.databinding.FragmentChecoutCreditBinding
import iti.mad.marketly.presentation.cart.CartViewModel
import iti.mad.marketly.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ChecoutFragmentCredit : Fragment() {
    lateinit var paymentSheet: PaymentSheet
    lateinit var binding: FragmentChecoutCreditBinding
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    var totalAmount = 0.0

    private val cartViewModel by viewModels<CartViewModel>{
        CartViewModel.Factory
    }
    private val stripeViewModel by viewModels<StripeViewModel>{
        StripeViewModel.Factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this,::onPaymentSheetResult)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChecoutCreditBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalAmount = 0.0
        binding.appBar.backArrow.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })

        val draftOrderLineItemList = DraftOrderManager.getLineList()
        val draftOrderAddress = DraftOrderManager.getShippingAddress()
        val draftOrderCustomer = DraftOrderManager.getCustomer()
        val draftAppliedDiscount = DraftOrderManager.getDiscount()
        var currency:String = SharedPreferenceManager.getSavedCurrency(requireContext()).toString()


        for (item in draftOrderLineItemList){
            totalAmount =totalAmount+item.price.toDouble()

        }
        if(currency == null||currency == ""){
            currency = "USD"
        }

        binding.countryName.text = draftOrderAddress.country
        binding.city.text = draftOrderAddress.city
        binding.discountText.text = draftAppliedDiscount.title
        binding.discountValueFromApi.text = draftAppliedDiscount.value
        if(AdsManager.clipBoardCode.code.isEmpty()||AdsManager.clipBoardCode.code.equals("DUMMY")){
            totalAmount = totalAmount
        }else{
            if(AdsManager.value.toInt() == 100){
                totalAmount = totalAmount
            }else{
                val percentage = (totalAmount)*(AdsManager.value/100)
                totalAmount = totalAmount - percentage
            }
        }


        if(SettingsManager.getCurrncy()=="EGP"){
            binding.totalAmountTextView.text= CurrencyConverter.switchToEGP(totalAmount.toString(), binding.totalAmountTextView.id)+" LE"
        }else{
            binding.totalAmountTextView.text= CurrencyConverter.switchToUSD(totalAmount.toString(), binding.totalAmountTextView.id)+" $"
        }


        val method = {
            PaymentConfiguration.init(requireContext(),"pk_test_51NLhznEtHa5WFOrtsgXSsAKfgIRP64BiI0Sv5yMGEVQoSVW55Ml0VlOe9LdiLrXfdxi4Hc7b9meVFIG3mylOIRTj00t69BIBHK")
            presentPaymentSheet(StripeConfigs.getPaymentKey(),customerConfig)

        }
        stripeViewModel.createUser()
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                stripeViewModel._customerCreationResponce.collect{
                    when(it){
                        is ResponseState.OnLoading->{

                        }
                        is ResponseState.OnSuccess->{
                            StripeConfigs.setCustomerConfig(it.response.id)
                            createKey(it.response.id)
                        }
                        is ResponseState.OnError->{

                            Log.i(ContentValues.TAG, "onViewCreated:${it.message} ")
                        }
                        else ->{}
                    }
                }
            }
        }
        binding.placeOrder.setOnClickListener(View.OnClickListener {

            AlertManager.functionalDialog("Order Confermation",requireContext(),"Do you want to checkout?",method).show()

        })
    }
    fun presentPaymentSheet(paymentIntentClientSecret:String,customerConfig:PaymentSheet.CustomerConfiguration) {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult){
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                val orderID = System.currentTimeMillis().toString()
                var quant = 0
                for (oreder in DraftOrderManager.getOrder()){
                    quant+=oreder.numberOfItems.toInt()
                }
                val order = OrderModel(
                    orderID,
                    DraftOrderManager.getOrder(),
                    quant,
                    DateFormatter.getCurrentDate(),
                    totalAmount,
                    AdsManager.clipBoardCode.code,
                    AdsManager.value.toString(),
                    DraftOrderManager.getShippingAddress().address1
                )
                cartViewModel.saveProuctsInOrder(order)
                val action = ChecoutFragmentCreditDirections.actionChecoutFragmentCreditToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }
    fun createKey(customerKey:String){
        stripeViewModel.createKey(customerKey)
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                stripeViewModel._keyCreationResponce.collect{
                    when(it){
                        is ResponseState.OnLoading->{

                        }
                        is ResponseState.OnSuccess->{
                            StripeConfigs.setKey(it.response.id)
                            createPaymentIntent(StripeConfigs.getCustomerConfig(),totalAmount.toInt(),SettingsManager.getCurrncy())
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
    fun createPaymentIntent(customerID:String,amount:Int,currency:String){
        var cur = ""
        if(SettingsManager.getCurrncy()=="EGP"){
            cur = "EGP"
        }else{
            cur="USD"
        }
        
        if (totalAmount == 0.0){
            totalAmount = .01
        }
        stripeViewModel.createPaymentIntent(customerID,amount*100,cur,autoPaymentMethodsEnable = true)
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                stripeViewModel._paymentIntent.collect{
                    when(it){
                        is ResponseState.OnLoading->{

                        }
                        is ResponseState.OnSuccess->{
                            StripeConfigs.setPaymentKey(it.response.clientSecret)
                            customerConfig = PaymentSheet.CustomerConfiguration(
                                StripeConfigs.getCustomerConfig(),StripeConfigs.getKey()
                            )
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


}
