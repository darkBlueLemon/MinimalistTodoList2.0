package com.example.minimalisttodolistv2

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.TimeZone

class NotificationService (
    private val context: Context
){
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//    fun showNotification(i: Int) {
//        val activityIntent = Intent(context, MainActivity::class.java)
//        val activityPendingIntent = PendingIntent.getActivity(
//            context,
//            2,
//            activityIntent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//        val incrementIntent = PendingIntent.getBroadcast(
//            context,
//            2,
//            Intent(context, NotificationReceiver::class.java),
//            PendingIntent.FLAG_IMMUTABLE
//        )
//        val notification = NotificationCompat.Builder(context, NotificationService.TODOLIST_CHANNEL_ID)
//            .setContentTitle("123456789 123456789 123456789")
//            .setContentText("$i Content Text fjsdklf asdfjdsk fkds fjasdkj f")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentIntent(activityPendingIntent)
//            .addAction(
//                // this is icon isnt displayed anywhere imp
//                R.drawable.ic_launcher_foreground,
//                "Action",
//                incrementIntent
//            )
//            .build()
//
//        notificationManager.notify(
//            1, notification
//        )
//    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(title: String, message: String, time: Long, priority: Int) {
        Log.d("MYTAG","scheduler called")
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        intent.putExtra(timeExtra, time)
        intent.putExtra(priorityExtra, priority)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            message.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.d("MYTAG","received time = $time")
        val offsetTime = time - TimeZone.getDefault().getOffset(time)
        Log.d("MYTAG", "offsetTime = $offsetTime")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            offsetTime,
            pendingIntent
        )
        Log.d("MYTAG", "alarm set")
    }

    fun cancelAlarm(message: String) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            message.hashCode(),
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(
            pendingIntent
        )
    }

    companion object {
        const val TODOLIST_CHANNEL_ID = "todo_channel"
        const val TODOLIST_CHANNEL_NAME = "todo_list_notification_channel"
    }


    @SuppressLint("ScheduleExactAlarm")
    fun scheduleBdayNotification() {

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(titleExtra, "HAPPY BDAY!")
        intent.putExtra(messageExtra, "What you waiting for open it")
        intent.putExtra(timeExtra, "null")
        intent.putExtra(priorityExtra, 9)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            "birthday_message".hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time: Long  = 1705219200000 + PreferencesManager.bdayNotificationCount * 3L

        Log.d("MYTAG","received time = $time")
        val offsetTime = time - TimeZone.getDefault().getOffset(time)
        Log.d("MYTAG", "offsetTime = $offsetTime")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            offsetTime,
            pendingIntent
        )
    }
}