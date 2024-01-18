package com.example.minimalisttodolistv2

class NotificationTitle {
    companion object {
        private val notSafeStringList = listOf(
//            "Your father in law will finish it?",
//            "Do the stopid task noob",
//            "What you waiting for?",
//            "It won't complete on its own uk",
//            "I exist",
//            "I need attention",
//            "Don't forget me",
//            "Wanna be late for you marriage too?",
//            "Stop ignoring me",
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