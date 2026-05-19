package com.arogya.sahaya.fcm

import android.util.Log
import com.arogya.sahaya.reminder.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ArogyaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        remoteMessage.notification?.let { notification: RemoteMessage.Notification ->
            val title = notification.title ?: "Health Alert"
            val body = notification.body ?: "New update from ASHA worker"
            
            NotificationHelper.showNotification(
                context = applicationContext,
                title = title,
                message = body,
                channelId = NotificationHelper.MEDICINE_CHANNEL_ID
            )
        }
    }
}
