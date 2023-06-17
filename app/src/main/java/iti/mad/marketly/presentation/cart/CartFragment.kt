package iti.mad.marketly.presentation.cart

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import iti.mad.marketly.R
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.databinding.FragmentCartBinding
import iti.mad.marketly.databinding.FragmentProductDetailsBinding
import iti.mad.marketly.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.math.BigDecimal


class CartFragment : Fragment(), CartFragmentInterface {
    lateinit var binding: FragmentCartBinding
    private val cartViewModel by viewModels<CartViewModel> {
        CartViewModel.Factory
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        val paymentConfermation: PaymentConfirmation? = it.data?.getParcelableExtra(
            PaymentActivity.EXTRA_RESULT_CONFIRMATION,
            PaymentConfirmation::class.java)
        if (paymentConfermation != null) {
            val paymentDetails = paymentConfermation.toJSONObject().toString()
            val jObject: JSONObject = JSONObject(paymentDetails)
        }
    }
    lateinit var adapters: CartAdapter
    var cartItems: MutableList<CartModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var configuration: PayPalConfiguration
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapters = CartAdapter(requireContext(), this)
        cartViewModel.getAllCart()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel._cartResponse.collect {

                    when (it) {
                        is ResponseState.OnLoading -> {
                        }
                        is ResponseState.OnSuccess -> {
                            cartItems = it.response.toMutableList()
                            adapters.submitList(cartItems)
                            binding.categoryProductRecView.apply {
                                adapter = adapters
                                layoutManager = LinearLayoutManager(context).apply {
                                    orientation = RecyclerView.VERTICAL
                                }
                            }

                        }
                        is ResponseState.OnError -> {
                            Log.i(
                                ContentValues.TAG,
                                "onViewCreated:${it.message} this is an errrror"
                            )
                        }
                        else -> {}
                    }

                }
            }
        }
        binding.checkoutBtn.setOnClickListener(View.OnClickListener {
            val itemCount = adapters.itemCount
            val currentItems = adapters.currentList
            var totalPrice = 0.0
            for (items in currentItems) {
                totalPrice += items.price
            }
            val methodADS = {
                val percentage = (totalPrice * 0.10)
                totalPrice = totalPrice - percentage
                val orderID = System.currentTimeMillis().toString()
                val order = OrderModel(orderID, currentItems, itemCount, DateFormatter.getCurrentDate())
                cartViewModel.saveProuctsInOrder(order)
                configuration =
                    PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                        .clientId(Constants.CLIENT_ID)
                getPayment(totalPrice.toString(), "USD")
            }
            if(!AdsManager.clipBoardCode.equals("")){
                AlertManager.functionalDialog("Use Code",requireContext(),"Do you like to use the code saved in your clipboard?",methodADS)
                    .show()

            }else{
                val orderID = System.currentTimeMillis().toString()
                val order = OrderModel(orderID, currentItems, itemCount, DateFormatter.getCurrentDate())
                cartViewModel.saveProuctsInOrder(order)
                configuration =
                    PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                        .clientId(Constants.CLIENT_ID)
                getPayment(totalPrice.toString(), "USD")
            }

        })
    }

    override fun onDelete(cartModel: CartModel) {
        val routine = {
            cartViewModel.deleteCartItem(cartModel.id.toString())
            cartItems.remove(cartModel)
            adapters.submitList(cartItems)
            Toast.makeText(requireContext(), "DELETED", Toast.LENGTH_LONG).show()
        }

        AlertManager.functionalDialog(
            "Deleting CartItem", requireContext(), "Are you sure you want to delete this?",
            routine
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getPayment(amount: String, code: String) {
        var payment = PayPalPayment(
            BigDecimal(amount),
            code,
            "Code with Arvind",
            PayPalPayment.PAYMENT_INTENT_SALE
        )
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        if (this::configuration.isInitialized) {
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
        }
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        launcher.launch(intent)
    }
}
