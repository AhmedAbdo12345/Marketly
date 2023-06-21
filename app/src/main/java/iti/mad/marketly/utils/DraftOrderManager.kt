package iti.mad.marketly.utils

import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.draftorder.*


object DraftOrderManager {
    private lateinit var draftOrderBody: DraftOrderBody
    private lateinit var lineItems: List<LineItems>
    private lateinit var address: ShippingAddress
    private lateinit var customer: Customer
    private lateinit var appliedDiscount: AppliedDiscount
    private lateinit var orders:MutableList<CartModel>
    private var draftOrderID:Long = 0
    private var direction = 0
    private var creditNumber:Long = 0
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
    fun gotoCredit(){
        direction =1
    }
    fun gotoPay(){
        direction = 0
    }
    fun getDirection():Int{
        return direction
    }
    fun checkCreditCard():Boolean{
        if(creditNumber == 0L){
            return false
        }else {
            return true
        }
    }
    fun setCreidet(number:Long){
        creditNumber = number
    }
    fun clearCredit(){
        creditNumber = 0
    }
fun setOrder(list:MutableList<CartModel>){
    orders = list
}
    fun getOrder():MutableList<CartModel>{
        return orders
    }
}