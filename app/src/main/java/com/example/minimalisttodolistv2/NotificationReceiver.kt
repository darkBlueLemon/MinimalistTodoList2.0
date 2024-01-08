package com.example.minimalisttodolistv2

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.minimalisttodolistv2.NotificationTitle.Companion.getNotificationTitle

// Changed
const val titleExtra = "title extra"
const val messageExtra = "message extra"
const val timeExtra = "time extra"
const val priorityExtra = "priority extra"

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("MYTAG", "receiver called")
//        val service = NotificationService(context)
//        service.showNotification(++Counter.value)

//        val notificationIntent = Intent(context, MainActivity::class.java)
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            666,
//            notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        // Create an explicit intent for an Activity in your app.
        val intent2 = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
//        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 3, intent2, PendingIntent.FLAG_IMMUTABLE)
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 3, intent2, PendingIntent.FLAG_IMMUTABLE)

        // Changed
        val notification = NotificationCompat.Builder(context, NotificationService.COUNTER_CHANNEL_ID)
            .setContentText(intent?.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // This will make the notification disappear when clicked
            .setContentTitle(getNotificationTitle())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
//            .setContentTitle(intent?.getStringExtra(titleExtra))
//            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
//            .setContentIntent(pendingIntent) // Set the pending intent here

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(
//            1,
//            notification
//        )
        notificationManager.notify(
            intent?.getStringExtra(messageExtra).hashCode(),
            notification
        )

        // Set Alarm again
        val priority = intent?.getIntExtra(priorityExtra,0)!!
        var intervalMillis: Long = 60 * 1000L
        when (priority) {
            3 -> {
                intervalMillis = 60 * 1000L
            }
            2 -> {
                intervalMillis = 65 * 1000L
            }
            1 -> {
                intervalMillis = 70 * 1000L
            }
            0 -> {
                intervalMillis = 75 * 1000L
            }
        }

        val notificationService = NotificationService(context = context)
        notificationService.scheduleNotification(
            intent.getStringExtra(titleExtra)!!,
            intent.getStringExtra(messageExtra)!!, intent.getLongExtra( timeExtra, 0) + intervalMillis, priority)
    }
}