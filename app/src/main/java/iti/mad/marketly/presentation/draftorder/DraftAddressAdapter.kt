package iti.mad.marketly.presentation.draftorder

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import iti.mad.marketly.data.model.draftorder.ShippingAddress
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.databinding.RvAddressesListBinding
import iti.mad.marketly.presentation.settings.AddressFragmentInterface
import iti.mad.marketly.utils.DraftOrderManager

class DraftAddressAdapter (var context: Context?, val draftAddressInterface: DraftAddressInterface):
    ListAdapter<Address, DraftAddressViewHolder>(DraftAddressDiffUtil()) {
    lateinit var binding: RvAddressesListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftAddressViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= RvAddressesListBinding.inflate(inflater,parent,false)
        return DraftAddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DraftAddressViewHolder, position: Int) {
        val currentItem=getItem(position)
        binding.saveAddress.visibility = View.GONE
        binding.addressCountryCity.text = currentItem.Country+"/"+currentItem.City
        binding.addressStreet.text = currentItem.Street
        binding.DeleteAddress.setOnClickListener(View.OnClickListener {
            val address=ShippingAddress(currentItem.Country+"/"+currentItem.City+"/"+currentItem.Street,currentItem.City,currentItem.Country)
            DraftOrderManager.setAddress(address)
            Toast.makeText(context,"This Address has been set successfully",Toast.LENGTH_LONG).show()
            draftAddressInterface.onAddressSelected()
        })

    }

}