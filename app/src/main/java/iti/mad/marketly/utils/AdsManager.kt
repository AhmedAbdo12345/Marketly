package iti.mad.marketly.utils

import iti.mad.marketly.data.model.discount.DiscountCode

object AdsManager {
    var adsList:MutableList<DiscountCode> = mutableListOf()
    var value:Double=0.0
    var copounsUsed:MutableList<String> = mutableListOf()
    var clipBoardCode:String=""
    fun addDiscountList(discountCode: List<DiscountCode>){
        adsList = discountCode.toMutableList()
    }
    fun useCoupoune(code:String,productID:Long):Boolean{
        var coup=code+productID.toString()
        if (copounsUsed.contains(coup)){
            return false
        }else{
            copounsUsed.add(coup)
            return true
        }
    }
    fun useCode(code:String):Boolean{
        var flag=false
        for(i in 0..adsList.size-1){
            if(adsList.get(i).code.equals(code)){
                if(adsList.get(i).usage_count>0){
                    flag=true

                }
                break
            }
        }
        return flag
    }
    fun setValue(valu: String){
        val temp=valu.drop(1)
        value =temp.toDouble()
    }
    fun setClipBoard(code:String){
        clipBoardCode =code
    }
}