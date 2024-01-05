package com.example.minimalisttodolistv2

class NotificationTitle {
    companion object {
        private val notSafeStringList = listOf(
            "Your father in law will finish it?",
            "Do the stoopid task noob",
            "What you waiting for?",
            "It won't complete on its own uk"
        )

        private val safeStringList = listOf(
            "Task Overdue"
        )

        fun getNotificationTitle(): String {
            return safeStringList.random()
        }
    }
}