package iti.mad.marketly.utils

import iti.mad.marketly.data.model.draftorder.*


object DraftOrderManager {
    private lateinit var draftOrderBody: DraftOrderBody
    private lateinit var lineItems: List<LineItems>
    private lateinit var address: ShippingAddress
    fun buildDraftOrder(lineItems:List<LineItems>, customer: Customer,address: ShippingAddress
    ,cur:String,appliedDiscount: AppliedDiscount){
        draftOrderBody = DraftOrderBody(lineItems,customer,address,cur,appliedDiscount)
    }
    fun setItemList(list:List<LineItems>){
        lineItems =list
    }
    fun setAddress(addressShip: ShippingAddress){
        address = addressShip
    }
    fun getDraftOrder():DraftOrderBody{
        return draftOrderBody
    }
    fun getLineList():List<LineItems>{
        return lineItems
    }
    fun getShippingAddress():ShippingAddress{
        return address
    }

}