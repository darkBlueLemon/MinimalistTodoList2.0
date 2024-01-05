package com.example.minimalisttodolistv2

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

// Changed
const val titleExtra = "title extra"
const val messageExtra = "message extra"

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("MYTAG", "receiver called")
//        val service = NotificationService(context)
//        service.showNotification(++Counter.value)

        // Changed
        val notification = NotificationCompat.Builder(context, NotificationService.COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(
//            1,
//            notification
//        )
        notificationManager.notify(
            intent?.getStringExtra(titleExtra).hashCode(),
            notification
        )
    }
}