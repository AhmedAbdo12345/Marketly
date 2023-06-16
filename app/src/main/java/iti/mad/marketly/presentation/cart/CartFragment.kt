package iti.mad.marketly.presentation.cart

import android.content.ContentValues
import android.os.Bundle
import android.os.SystemClock
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
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.databinding.FragmentCartBinding
import iti.mad.marketly.databinding.FragmentProductDetailsBinding
import iti.mad.marketly.utils.AlertManager
import iti.mad.marketly.utils.DateFormatter
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CartFragment : Fragment(),CartFragmentInterface {
lateinit var binding: FragmentCartBinding
private val cartViewModel by viewModels<CartViewModel> {
    CartViewModel.Factory
}
lateinit var adapters:CartAdapter
    var cartItems:MutableList<CartModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapters = CartAdapter(requireContext(),this)
        cartViewModel.getAllCart()
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            repeatOnLifecycle(Lifecycle.State.STARTED){
                cartViewModel._cartResponse.collect{

                        when(it){
                            is ResponseState.OnLoading->{
                            }
                            is ResponseState.OnSuccess->{
                                cartItems= it.response.toMutableList()
                                adapters.submitList(cartItems)
                                binding.categoryProductRecView.apply {
                                    adapter = adapters
                                    layoutManager= LinearLayoutManager(context).apply {
                                        orientation= RecyclerView.VERTICAL
                                    }
                                }

                            }
                            is ResponseState.OnError->{
                                Log.i(ContentValues.TAG, "onViewCreated:${it.message} this is an errrror")
                            }
                            else ->{}
                        }

                    }
                }
            }
            binding.checkoutBtn.setOnClickListener(View.OnClickListener {
                val itemCount = adapters.itemCount
                val currentItems = adapters.currentList
                var totalPrice = 0.0
                for(items in currentItems){
                    totalPrice += items.price
                }
                val orderID = System.currentTimeMillis().toString()
                val order = OrderModel(orderID,currentItems,itemCount,DateFormatter.getCurrentDate(),totalPrice)
                cartViewModel.saveProuctsInOrder(order)
            })
        }

    override fun onDelete(cartModel: CartModel) {
        val routine= {
            cartViewModel.deleteCartItem(cartModel.id.toString())
            cartItems.remove(cartModel)
            adapters.submitList(cartItems)
            Toast.makeText(requireContext(),"DELETED", Toast.LENGTH_LONG).show()
        }

        AlertManager.functionalDialog("Deleting CartItem",requireContext(),"Are you sure you want to delete this?",
            routine).show()
    }
}
