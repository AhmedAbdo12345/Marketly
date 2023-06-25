package iti.mad.marketly.utils

object StripeConfigs {
    private var customerConfig = ""
    private var customerSecret = ""
    private var paymentKey = ""
    fun setCustomerConfig(sCustomerConfig:String){
        customerConfig = sCustomerConfig
    }
    fun getCustomerConfig():String{
        return customerConfig
    }
    fun setKey(key:String){
        customerSecret = key
    }
    fun getKey():String{
        return customerSecret
    }
    fun setPaymentKey(pKey:String){
        paymentKey=pKey
    }
    fun getPaymentKey():String{
        return paymentKey
    }
}