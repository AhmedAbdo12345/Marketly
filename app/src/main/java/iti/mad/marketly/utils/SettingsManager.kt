package iti.mad.marketly.utils

import iti.mad.marketly.data.model.settings.Address

object SettingsManager {
  private var currency:String= ""
  private var address:String = ""
  private var documentID:String = ""
  private var addresses:MutableList<Address> = mutableListOf()
  private var exchangeRate:Double=0.0
  private var userName = ""
  private var docIDS = mutableListOf<String>()
    fun curSetter(cur:String){
        if(cur==null){
            currency = ""
        }else{
            currency=cur
        }

    }
    fun userNameSetter(username:String){
        if(username == null){
            userName = ""
        }else {
            userName=username
        }

    }
    fun addressSetter(addr:String){
       if(addr == null){
           address = ""
       }else{
           address=addr
       }
    }
   fun exchangeRateSetter(rate:Double){
      if(rate == null){
         exchangeRate=0.0
      }else{
          exchangeRate=rate
      }
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
    fun fillDocIDs(ids:MutableList<String>){
        docIDS=ids
    }
fun getAddress():String = address
    fun getDocumentID():String = documentID
    fun getUserName():String = userName
    fun getCurrncy():String = currency
    fun getExchangeRate():Double = exchangeRate
    fun getIDS():MutableList<String> = docIDS
}