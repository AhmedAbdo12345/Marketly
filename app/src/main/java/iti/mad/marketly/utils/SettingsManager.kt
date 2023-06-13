package iti.mad.marketly.utils

import iti.mad.marketly.data.model.settings.Address

object SettingsManager {
   lateinit var currency:String
   lateinit var address:String//default
   var documentID:String = "Sonic@gmail.com"
   var addresses:MutableList<Address> = mutableListOf()
   var gotData=false
   var exchangeRate:Double=0.0

    fun curSetter(cur:String){
       currency=cur
    }
    fun addressSetter(addr:String){
       address=addr
    }
   fun exchangeRateSetter(rate:Double){
      exchangeRate=rate
   }
   fun fromUStoEGP(price:Double):Double{
       return price*exchangeRate
   }
    fun fromEGPtoUSD(price: Double):Double{
        return price/exchangeRate
    }
   fun documentIDSetter(email:String){
       documentID=email
   }

   fun fillAddress(address:Address){
      if(addresses==null){
          addresses= mutableListOf()
          addresses.add(address)
      }else{
          addresses.add(address)
      }
   }
    fun gotAddressesSuccesfully(){
        gotData=true
    }

}