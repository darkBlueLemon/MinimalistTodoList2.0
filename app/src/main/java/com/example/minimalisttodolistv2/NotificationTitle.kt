package com.example.minimalisttodolistv2

class NotificationTitle {
    companion object {
        private val notSafeStringList = listOf(
            "Task Due"
        )

        private val safeStringList = listOf(
            "Task Due"
        )

        fun getNotificationTitle(): String {
            return if (PreferencesManager.boringNotification) safeStringList.random() else notSafeStringList.random()
        }
    }
}