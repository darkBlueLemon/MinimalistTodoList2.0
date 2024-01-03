package com.example.minimalisttodolistv2

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationService (
    private val context: Context
){
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(i: Int) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            2,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, NotificationService.COUNTER_CHANNEL_ID)
            .setContentTitle("123456789 123456789 123456789")
            .setContentText("$i Content Text fjsdklf asdfjdsk fkds fjasdkj f")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(activityPendingIntent)
            .addAction(
                // this is icon isnt displayed anywhere imp
                R.drawable.ic_launcher_foreground,
                "Action",
                incrementIntent
            )
            .build()

        notificationManager.notify(
            1, notification
        )
    }

    companion object {
        const val COUNTER_CHANNEL_ID = "todo_channel"
    }
}