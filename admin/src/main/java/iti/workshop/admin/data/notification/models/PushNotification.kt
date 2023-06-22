package iti.workshop.admin.data.notification.models

import iti.workshop.admin.data.notification.models.NotificationData

data class PushNotification(
    val data: NotificationData,
    val to: String
)