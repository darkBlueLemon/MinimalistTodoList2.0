package com.example.minimalisttodolistv2

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

object PreferencesManager {
    private const val PREF_NAME = "todo_list_shared_pref_manager"
    private const val SORTING_ORDER = "sorting_order"
    private const val STAR_ICON = "start_icon_enabled"
    private const val THIN_FONT = "thin_font_enabled"
    private const val BORING_NOTIFICATION = "boring_notification"
    private const val BDAY_COUNT = "bday_count"
    private const val BDAY_VISIBLE = "bday_visible"
    private const val BDAY_OVER = "bday_over"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    }


    var starIcon: Boolean
        get() = sharedPreferences.getBoolean(STAR_ICON, true)
        set(value) {
            sharedPreferences.edit().putBoolean(STAR_ICON, value).apply()
        }

    var thinFont: Boolean
        get() = sharedPreferences.getBoolean(THIN_FONT, true)
        set(value) {
            sharedPreferences.edit().putBoolean(THIN_FONT, value).apply()
        }

    var sortingOrder: String
        get() = sharedPreferences.getString(SORTING_ORDER, "PRIORITY") ?: "PRIORITY"
        set(value) {
            sharedPreferences.edit().putString(SORTING_ORDER, value).apply()
        }

    var boringNotification: Boolean
        get() = sharedPreferences.getBoolean(BORING_NOTIFICATION, false)
        set(value) {
            sharedPreferences.edit().putBoolean(BORING_NOTIFICATION, value).apply()
        }

    // Bday
    var bdayNotificationCount: Int
        get() = sharedPreferences.getInt(BDAY_COUNT, 0)
        set(value) {
            sharedPreferences.edit().putInt(BDAY_COUNT, value).apply()
        }

    var bdayVisible: Boolean
        get() = sharedPreferences.getBoolean(BDAY_VISIBLE, false)
        set(value) {
            sharedPreferences.edit().putBoolean(BDAY_VISIBLE, value).apply()
        }

    var bdayOver: Boolean
        get() = sharedPreferences.getBoolean(BDAY_OVER, false)
        set(value) {
            sharedPreferences.edit().putBoolean(BDAY_OVER, value).apply()
        }
}