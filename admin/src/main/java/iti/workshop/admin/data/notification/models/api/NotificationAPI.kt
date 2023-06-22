package iti.workshop.admin.data.notification.models.api

import iti.workshop.admin.data.notification.models.api.APIRoutes.Companion.CONTENT_TYPE
import iti.workshop.admin.data.notification.models.api.APIRoutes.Companion.SERVER_KEY
import iti.workshop.admin.data.notification.models.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}