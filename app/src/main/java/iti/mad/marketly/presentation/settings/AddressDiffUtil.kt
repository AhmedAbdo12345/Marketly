package iti.mad.marketly.presentation.settings

import androidx.recyclerview.widget.DiffUtil
import iti.mad.marketly.data.model.settings.Address

class AddressDiffUtil: DiffUtil.ItemCallback<iti.mad.marketly.data.model.settings.Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem.AddressID == newItem.AddressID
    }

    override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem == newItem
    }
}