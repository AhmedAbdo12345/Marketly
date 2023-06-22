package iti.workshop.admin.presentation.features.coupon.ui.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.PriceRule
import iti.workshop.admin.data.notification.models.NotificationData
import iti.workshop.admin.data.notification.models.PushNotification
import iti.workshop.admin.data.notification.models.api.APIRoutes.Companion.TOPIC
import iti.workshop.admin.data.notification.models.api.RetrofitInstance
import iti.workshop.admin.databinding.CouponFragmentPriceRuleBinding
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.coupon.adapters.PriceRuleAdapter
import iti.workshop.admin.presentation.features.coupon.adapters.PriceRuleOnCLickListener
import iti.workshop.admin.presentation.features.coupon.ui.dialogs.AddPriceRuleDialog
import iti.workshop.admin.presentation.features.coupon.viewModel.CouponViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import iti.workshop.admin.presentation.utils.alertDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

@AndroidEntryPoint
class PriceRuleFragment : Fragment() {

    val viewModel:CouponViewModel by viewModels()
    lateinit var adapter: PriceRuleAdapter
    lateinit var binding:CouponFragmentPriceRuleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.coupon_fragment_price_rule, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = PriceRuleAdapter(PriceRuleOnCLickListener(::selectItem,::deleteItem,::onAddNotification))
        binding.mAdapter = adapter

        viewModel.retrievePriceRules()
        updateUISate()
        addPriceRuleAction()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onAddNotification(priceRule: PriceRule) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.edit_text)

        builder.setView(dialogLayout)
            .setPositiveButton("OK") { dialogInterface, i ->
                // Ok button clicked, do something with the input
                val inputText = editText.text.toString()
                lifecycleScope.launch {
                    val noification = PushNotification(
                        data = NotificationData(
                            title = "Discount Notificationi",
                            message =  inputText,
                            date = getCurrentDate(),
                            time = getCurrentTime(),
                            id = Random.nextInt()
                        ),
                        to = TOPIC

                    )
                    val response = async { RetrofitInstance.api.postNotification(noification)}
                    if (response.await().isSuccessful){
                        Toast.makeText(requireContext(), "Notification has been added", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "There is problem during send", Toast.LENGTH_SHORT).show()

                    }
                }

            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                // Cancel button clicked, do nothing
            }
            .show()
    }

    private fun addPriceRuleAction() {
        binding.floatingActionButton.setOnClickListener {
            val dialogFragment = AddPriceRuleDialog(viewModel)
            dialogFragment.show(requireActivity().supportFragmentManager, "AddPriceRuleDialog")
        }
    }

    private fun deleteItem(priceRule: PriceRule) {
        requireContext().alertDialog("Delete Action","Do you want delete ${priceRule.title} ? \n Are you sure?",{
            viewModel.deletePriceRule(priceRule) },{}
        )
    }

    private fun selectItem(priceRule: PriceRule) {
        val bundle = Bundle()
        bundle.putLong(ConstantsKeys.PRICE_RULE_ID_KEY, priceRule.id)
        findNavController().navigate(R.id.action_priceRuleFragment_to_discountCodeFragment,bundle)
    }

    private fun updateUISate() {
        lifecycleScope.launch {
            viewModel.priceRuleActionResponse.collect{ state ->
                state.first?.let {
                    Message.snakeMessage(
                        requireContext(),
                        binding.root,
                        state.second,
                        it
                    )?.show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.priceRuleResponse.collect{state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStates(DataStates.Error)
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                message,
                                false
                            )?.show()
                        }
                    }
                    is DataListResponseState.OnLoading -> {
                        dataViewStates(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStates(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStates(DataStates.Data)
                        adapter.submitList(state.data.price_rules)

                    }
                }
            }
        }

    }



    private fun dataViewStates(dataStates: DataStates) {
        when(dataStates){
            DataStates.Data -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.VISIBLE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Nothing -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.VISIBLE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Error -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.VISIBLE
            }
            DataStates.Loading ->{
                binding.shimmerResults.visibility = View.VISIBLE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate():String{
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth
        return "$year/$month/$day"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime():String{
        val currentTime = LocalTime.now()
        val hour = currentTime.hour
        val minute = currentTime.minute
        val second = currentTime.second
       return "$hour:$minute:$second"
    }



}