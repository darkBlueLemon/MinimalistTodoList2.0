package com.minimalisttodolist.pleasebethelastrecyclerview

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREF_NAME = "todo_list_shared_pref_manager"
    private const val SORTING_ORDER = "sorting_order"
    private const val STAR_ICON = "start_icon_enabled"
    private const val THIN_FONT = "thin_font_enabled"
    private const val BORING_NOTIFICATION = "boring_notification"
    private const val NOTIFICATION_PERMISSION_LAUNCHER_COUNT = "notification_permission_launcher_count"
    private const val CAN_DISPLAY_NOTIFICATION_PERMISSION_LAUNCHER = "can_display_notification_permission_launcher"

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

    var canDisplayNotificationPermission: Boolean
        get() = sharedPreferences.getBoolean(CAN_DISPLAY_NOTIFICATION_PERMISSION_LAUNCHER, true)
        set(value) {
            sharedPreferences.edit().putBoolean(CAN_DISPLAY_NOTIFICATION_PERMISSION_LAUNCHER, value).apply()
        }

    var updateNotificationPermissionCount: Int
        get() = sharedPreferences.getInt(NOTIFICATION_PERMISSION_LAUNCHER_COUNT, 0)
        set(value) {
            sharedPreferences.edit().putInt(NOTIFICATION_PERMISSION_LAUNCHER_COUNT, value).apply()
        }
}