package com.nativelocalstorage
import com.rn_turbo_module.R


import android.content.SharedPreferences
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.turbomodule.core.interfaces.TurboModule
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager


class NativeNotificationModule(reactContext: ReactApplicationContext) : NativeNotificationSpec(reactContext) {

  private val CHANNEL_ID = "coffee_status"
  private val NOTIFICATION_ID = 42

  private val notificationManager =
    reactContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  init {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Coffee Status",
        NotificationManager.IMPORTANCE_DEFAULT
      )
      notificationManager.createNotificationChannel(channel)
    }
  }

  override fun getName() = NAME

  override fun showPreparing(): String {
    // Проверяем разрешение на уведомления для Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      val permission = ContextCompat.checkSelfPermission(
        reactApplicationContext, 
        Manifest.permission.POST_NOTIFICATIONS
      )
      
      if (permission != PackageManager.PERMISSION_GRANTED) {
        return "Permission denied: POST_NOTIFICATIONS required"
      }
    }

    // Создаем кастомный RemoteViews для уведомления
    val customView = RemoteViews(reactApplicationContext.packageName, R.layout.notification_layout)
    
    // Настраиваем элементы кастомного layout
    customView.setTextViewText(R.id.notification_title, "☕ Готовим ваш кофе!")
    
    // Устанавливаем простой неопределенный прогресс
    customView.setProgressBar(R.id.progress_bar, 100, 0, true)

    // Создаем уведомление с кастомным layout
    val notification: Notification = NotificationCompat.Builder(reactApplicationContext, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_popup_reminder)
        .setCustomContentView(customView) // Используем кастомный layout
        .setCustomBigContentView(customView) // Для развернутого состояния
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOngoing(true)
        .setColor(0xFF8A00.toInt())
        .setVibrate(longArrayOf(0, 200, 100, 200))
        .setLights(0xFF8A00.toInt(), 1000, 1000)
        .setAutoCancel(false) // Не исчезает при нажатии
        .build()

    // Показываем уведомление с нативным неопределенным прогрессом
    notificationManager.notify(NOTIFICATION_ID, notification)
    
    return "Custom notification with native indeterminate progress shown"
  }

  companion object {
    const val NAME = "NativeNotification"
  }
}