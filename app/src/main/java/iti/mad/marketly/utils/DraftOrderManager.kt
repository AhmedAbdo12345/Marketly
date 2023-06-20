package iti.mad.marketly.utils

import iti.mad.marketly.data.model.draftorder.*


object DraftOrderManager {
    private lateinit var draftOrderBody: DraftOrderBody
    private lateinit var lineItems: List<LineItems>
    private lateinit var address: ShippingAddress
    private lateinit var customer: Customer
    private lateinit var appliedDiscount: AppliedDiscount
    private var draftOrderID:Long = 0
    fun buildDraftOrder(lineItems:List<LineItems>, customer: Customer,address: ShippingAddress
    ,cur:String,appliedDiscount: AppliedDiscount):DraftOrderBody{
        draftOrderBody = DraftOrderBody(lineItems,customer,address,cur,appliedDiscount)
        return draftOrderBody
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
    fun setCustomer(customerD: Customer){
        customer = customerD
    }
    fun getCustomer():Customer{
        return customer
    }
fun setDiscount(appliedDiscount: AppliedDiscount){
    this.appliedDiscount = appliedDiscount
}
    fun getDiscount():AppliedDiscount{
        if(this::appliedDiscount.isInitialized){
            return appliedDiscount
        }else{
            return AppliedDiscount("0","dummy discount","dummy","0")
        }
    }
    fun setDraftOrderID(id:Long){
        draftOrderID = id
    }
    fun getDraftOrderID():Long{
        return draftOrderID
    }

}