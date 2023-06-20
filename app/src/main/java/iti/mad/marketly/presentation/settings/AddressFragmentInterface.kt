package iti.mad.marketly.presentation.settings

interface AddressFragmentInterface {
    fun onDelete(address: iti.mad.marketly.data.model.settings.Address)
    fun onSetAsDefault(address: iti.mad.marketly.data.model.settings.Address)
}