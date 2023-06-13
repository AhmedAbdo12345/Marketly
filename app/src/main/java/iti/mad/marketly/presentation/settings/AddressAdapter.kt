package iti.mad.marketly.presentation.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import iti.mad.marketly.databinding.RvAddressesListBinding

class AddressAdapter(var context: Context?):
    ListAdapter<iti.mad.marketly.data.model.settings.Address, AddressViewHolder>(AddressDiffUtil()) {
    lateinit var binding:RvAddressesListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= RvAddressesListBinding.inflate(inflater,parent,false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentItem=getItem(position)
        holder.binding.addressCountryCity.text=currentItem.Country +"/"+currentItem.City
        holder.binding.addressStreet.text=currentItem.Street

    }
}