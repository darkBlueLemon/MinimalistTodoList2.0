package com.example.minimalisttodolistv2

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.core.app.NotificationCompat
import com.example.minimalisttodolistv2.NotificationTitle.Companion.getNotificationTitle

// Changed
const val titleExtra = "title extra"
const val messageExtra = "message extra"
const val timeExtra = "time extra"
const val priorityExtra = "priority extra"

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        //Creating shared prefs
        PreferencesManager.initialize(context)

        Log.d("MYTAG", "receiver called")

        // Create an explicit intent for an Activity in your app.
        val intent2 = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 3, intent2, PendingIntent.FLAG_IMMUTABLE)

        // Grouping Notifications together
        val SUMMARY_ID = 0
        val GROUP_KEY = "com.android.minimalisttodolistv2_TASK"

        // Changed
        val notification = NotificationCompat.Builder(context, NotificationService.TODOLIST_CHANNEL_ID)
            .setContentText(intent?.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // This will make the notification disappear when clicked
            .setContentTitle(getNotificationTitle())
            .setSmallIcon(R.drawable.icon5)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(GROUP_KEY)
            .build()

        val summaryNotification = NotificationCompat.Builder(context, NotificationService.TODOLIST_CHANNEL_ID)
            .setContentText("")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // This will make the notification disappear when clicked
            .setContentTitle("")
            // Set content text to support devices running API level < 24.
            .setSmallIcon(R.drawable.icon5)
            // Build summary info into InboxStyle template.
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("")
                .addLine("")
                .setBigContentTitle("")
                .setSummaryText(""))
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Specify which group this notification belongs to.
            .setGroup(GROUP_KEY)
            // Set this notification as the summary for the group.
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setGroupSummary(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Calling summary notification
        notificationManager.notify(
//            (intent?.getStringExtra(messageExtra).hashCode()).hashCode(),
            456,
            summaryNotification
        )


        val priority = intent?.getIntExtra(priorityExtra,0)!!
        if(priority == 9) {
            val notification = NotificationCompat.Builder(context, NotificationService.TODOLIST_CHANNEL_ID)
                .setContentText("What you waiting for open it?")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // This will make the notification disappear when clicked
                .setContentTitle("HAPPY BDAY!!")
                .setSmallIcon(R.drawable.icon5)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(GROUP_KEY)
                .build()

            // Calling lone notification
            notificationManager.notify(
                intent.getStringExtra(messageExtra).hashCode(),
                notification
            )
//            if(!PreferencesManager.bdayOver) {
            PreferencesManager.bdayVisible = true
//            if(!PreferencesManager.bdayOver) PreferencesManager.bdayVisible = true

            if (PreferencesManager.bdayNotificationCount >= 3) return
            PreferencesManager.bdayNotificationCount =
                PreferencesManager.bdayNotificationCount + 1
            val notificationService = NotificationService(context = context)
            notificationService.scheduleBdayNotification()
            Log.d("MYTAG", "called bday notifier")
            return
//            }
        }

        // Calling lone notification
        notificationManager.notify(
            intent.getStringExtra(messageExtra).hashCode(),
            notification
        )

        // Set Alarm again
        var intervalMillis: Long = 60 * 1000L
        when (priority) {
            3 -> {
                intervalMillis = 60 * 60 * 1000L
            }
            2 -> {
                intervalMillis = 6 * 60 * 60 * 1000L
            }
            1 -> {
                intervalMillis = 12 * 60 * 60 * 1000L
            }
            0 -> {
                intervalMillis = 24 * 60 * 60 * 1000L
            }
        }


        val notificationService = NotificationService(context = context)
        notificationService.scheduleNotification(
            intent.getStringExtra(titleExtra)!!,
            intent.getStringExtra(messageExtra)!!, intent.getLongExtra( timeExtra, 0) + intervalMillis, priority)
    }
}