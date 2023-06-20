package iti.workshop.admin.presentation.features.auth.model

import java.io.Serializable

data class User(
    var id:String?=null,
    var name:String?=null,
    var email:String?=null,
    var image:String?=null,
    var phone:String?=null,
    var jopTitle:JopTitle?=null
//    var activities:List<Activity>?=null
):Serializable

//data class Activity(
//    val action:String,
//    val date:String,
//    val time:String
//)

enum class JopTitle{
    SuperUser,Admin,SuperVisor,User
}