package iti.mad.marketly.utils

import android.util.Log

object CurrencyConverter {
   private lateinit var currencySwitch :String
    private var textIDSEGP = mutableListOf<Int>()
    private var textIDSUSD = mutableListOf<Int>()
    fun setSwitch(value:String){
        if (!this::currencySwitch.isInitialized){
            currencySwitch = value
        }else if(currencySwitch !=value){
            currencySwitch = value
            if (value == "EGP"){
                textIDSEGP.clear()
            }else if(value == "USD"){
                textIDSUSD.clear()
            }
        }
    }
    fun switchToUSD(price:String, id:Int):String {
        var tempPrice = price
        tempPrice = ((price.toDouble()/SettingsManager.getExchangeRate()).toFloat()).toString()
//        if(!this::currencySwitch.isInitialized){
//            if(SettingsManager.getCurrncy() == "USD"&& !textIDSUSD.contains(id)){
//                tempPrice = ((price.toDouble()/SettingsManager.getExchangeRate()).toFloat()).toString()
//                Log.i("Currncy", "switchToUSD: SWITCHED TO ${tempPrice}")
//                textIDSUSD.add(id)
//            }
//        }else{
//            if(currencySwitch == "USD"&& !textIDSUSD.contains(id)){
//                tempPrice = ((price.toDouble()/SettingsManager.getExchangeRate()).toFloat()).toString()
//                Log.i("Currncy", "switchToUSD: SWITCHED TO ${tempPrice}")
//                textIDSUSD.add(id)
//            }
//        }
        return tempPrice
    }
    fun switchToEGP(price:String, id:Int):String {
        var tempPrice = price
        //tempPrice = ((price.toDouble()*SettingsManager.getExchangeRate()).toFloat()).toString()
//        if(!this::currencySwitch.isInitialized){
//            if(SettingsManager.getCurrncy() == "EGP"&& !textIDSEGP.contains(id)){
//                tempPrice = ((price.toDouble()*SettingsManager.getExchangeRate()).toFloat()).toString()
//                textIDSEGP.add(id)
//            }
//        }else{
//            if(currencySwitch == "EGP"&& !textIDSEGP.contains(id)){
//                tempPrice = ((price.toDouble()*SettingsManager.getExchangeRate()).toFloat()).toString()
//                textIDSEGP.add(id)
//            }
//        }
        return tempPrice
    }


}