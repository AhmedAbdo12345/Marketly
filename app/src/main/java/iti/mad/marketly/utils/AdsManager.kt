package iti.mad.marketly.utils

import iti.mad.marketly.data.model.discount.DiscountCode
import iti.mad.marketly.data.model.pricingrules.PriceRule

object AdsManager {
   private var adsList:MutableList<DiscountCode> = mutableListOf()
    private var ruleList:MutableList<PriceRule> = mutableListOf()
    var value:Double=0.0

    var clipBoardCode:DiscountCode= DiscountCode("DUMMY","",0,0,"",0)
    fun addDiscountList(discountCode: List<DiscountCode>){
        adsList = discountCode.toMutableList()
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
        var temp = valu
        if(temp == "-100"){
            temp = "-10"
        }
        temp=temp.drop(1)

        value =temp.toDouble()
        if(value == 100.0){
            value = 10.0
        }
    }
    fun setClipBoard(ads:DiscountCode){
        clipBoardCode =ads
    }
    fun setPriceLists(priceList:MutableList<PriceRule>){
        ruleList = priceList
    }
    fun getPriceList():MutableList<PriceRule>{
        return ruleList
    }
    fun appendAdd(ads:MutableList<DiscountCode>){
        adsList += ads
    }
    fun getAdsList():MutableList<DiscountCode>{
        return adsList
    }
    fun clearClip(){
        clipBoardCode = DiscountCode("","",0,0,"",1)
    }
}