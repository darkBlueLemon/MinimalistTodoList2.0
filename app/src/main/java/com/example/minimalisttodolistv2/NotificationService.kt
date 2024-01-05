package com.example.minimalisttodolistv2

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.sql.Time
import java.time.Instant
import java.time.ZoneId
import java.util.TimeZone
import kotlin.concurrent.timer

class NotificationService (
    private val context: Context,
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

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(title: String, message: String, time: Long) {
        Log.d("MYTAG","scheduler called")
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            1,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            title.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // converting time in millis from utc to local time
        // doesnt work for api < 26
//        val localDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Instant.ofEpochMilli(time)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime()
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }

        // Convert LocalDateTime to milliseconds
//        val localDateTimeMillis = localDateTime.atZone()
//            .toInstant()
//            .toEpochMilli()

//        val timeFormat = android.text.format.Time()
//        timeFormat.set(time + TimeZone.getDefault().getOffset(time) )
//        timeFormat = timeFormat.toMillis(true)



        // time in millis
//        val time = time
//        val time = 1704357640000
//        Log.d("MYTAG",localDateTimeMillis.toString())
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

    companion object {
        const val COUNTER_CHANNEL_ID = "todo_channel"
    }
}