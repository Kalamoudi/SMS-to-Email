package com.example.smstoemail.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.smstoemail.MainActivity
import com.example.smstoemail.R
import com.example.smstoemail.sharedPrefs


class BackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand")

        createForegroundNotification()


        // Note that this method is called each time the service is started, so be careful with long-running tasks.
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service onDestroy")

        // Stop the foreground service and remove the notification
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
    }

    companion object {
        private const val TAG = "BackgroundService"
    }

    private fun createForegroundNotification() {
        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("SMS To Email is running in the background")
            .setContentText("Tap to open the app.")
            .setSmallIcon(R.drawable.ic_notification_icon_background)

        // Make the notification persistent
        builder.setOngoing(true)

        // Launch the main activity when the notification is tapped
        val mainIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        // Add an action to open the app's settings page
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        settingsIntent.data = uri
        val settingsPendingIntent = PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        builder.addAction(resources.getIdentifier("drawable/icons/settings_black_24dp", null, packageName),
            "Close app in settings",
            settingsPendingIntent)


        // Start the service as a foreground service with the notification
        startForeground(1, builder.build())
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "Service onTaskRemoved - App was force-stopped from the settings")

        // Save a flag to SharedPreferences indicating the app was force-stopped
        //val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("app_force_stopped", true)
//        editor.apply()

        sharedPrefs.edit().putBoolean("app_force_stopped", true).apply()


    }

}