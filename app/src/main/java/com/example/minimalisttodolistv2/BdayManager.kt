package com.example.minimalisttodolistv2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.setContent
import com.example.minimalisttodolistv2.ui.theme.MinimalistTodoListV2Theme

class BdayManager: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MYTAG", "bday manager called")

        val sharedPref = getSharedPreferences("Bday_manager", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Saving
        val saveText = "some text you want to save"
        val saveText2 = "some text you want to save2"

        editor.apply {
            putString("key1", saveText)
            putString("key2", saveText2)
            apply()
        }

        // Reading
        sharedPref.getString("key1", "default String")
        sharedPref.getString("key2", null)

        val isItTimeYet = sharedPref.getBoolean("isItTimeYet", false)

        if (isItTimeYet) {
        }
    }
}