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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.R
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.draftorder.LineItems
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentCartBinding
import iti.mad.marketly.databinding.FragmentProductDetailsBinding
import iti.mad.marketly.presentation.settings.AddressListFragmentDirections
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


    lateinit var adapters: CartAdapter
    var cartItems: MutableList<CartModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


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
                            var idsList = mutableListOf<String>()
                            for (ids in it.response){
                                idsList.add(ids.id.toString())
                            }
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
            val lineItemList = mutableListOf<LineItems>()
            for (items in currentItems) {
                totalPrice += items.price
                lineItemList.add(LineItems(items.price.toString(),items.id.toInt(),items.quantity.toInt(),"Custom "+items.title))
            }
            DraftOrderManager.setItemList(lineItemList)
            var customerEmail = "Sonic@gmail.com"
            if(SharedPreferenceManager.getUserMAil(requireContext())!=null){
                customerEmail = SharedPreferenceManager.getUserMAil(requireContext()).toString()
            }
            var customerName = "deff"
            if(SharedPreferenceManager.getUserName(requireContext())!=null){
                customerName = SharedPreferenceManager.getUserName(requireContext()).toString()
            }
            var customerID = "123"
            if(SharedPreferenceManager.getUserID(requireContext())!=null){
                customerID = SharedPreferenceManager.getUserID(requireContext()).toString()
            }
            val customer = iti.mad.marketly.data.model.draftorder.Customer(customerID.toLong(),false)
            DraftOrderManager.setCustomer(customer)
            val orderID = System.currentTimeMillis().toString()
            val order = OrderModel(orderID, currentItems, itemCount, DateFormatter.getCurrentDate())
            cartViewModel.saveProuctsInOrder(order)
            var action= CartFragmentDirections.actionCartFragment2ToDraftAddressFragment()
            findNavController().navigate(action)


        })
    }

    override fun onDelete(cartModel: CartModel) {
        val routine = {
            cartViewModel.deleteCartItem(cartModel.id.toString())
            cartItems.remove(cartModel)
            adapters.submitList(cartItems)
            binding.categoryProductRecView.adapter = adapters
            binding.categoryProductRecView.layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }


        AlertManager.functionalDialog(
            "Deleting CartItem", requireContext(), "Are you sure you want to delete this?",
            routine
        ).show()


    }


}
