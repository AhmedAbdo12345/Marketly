package iti.workshop.admin.data.notification.models


data class NotificationData(
    val id: Int?,
    var title: String,
    var message: String,
    var date: String,
    var time: String
)