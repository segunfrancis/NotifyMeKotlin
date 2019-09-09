package com.android.segunfrancis.notifymekotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    private lateinit var buttonNotify: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonCancel: Button
    private lateinit var mNotifyManager: NotificationManager
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private val NOTIFICATION_ID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* VERY IMPORTANT */
        createNotificationChannel()

        buttonNotify = findViewById(R.id.notify)
        buttonNotify.setOnClickListener {
            sendNotification()
        }

        buttonUpdate = findViewById(R.id.update)
        buttonUpdate.setOnClickListener {
            updateNotification()
        }

        buttonCancel = findViewById(R.id.cancel)
        buttonCancel.setOnClickListener {
            cancelNotification()
        }
        setNotificationButtonState(true, false, false)
    }

    private fun sendNotification() {
        var notifyBuilder: NotificationCompat.Builder = getNotificationBuilder()
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(false, true, true)
    }

    private fun createNotificationChannel() {
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /* Notification Channels are required for android 26 and above */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create Notification Channel
            var notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mascot"
            mNotifyManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        var notificationIntent = Intent(this, MainActivity::class.java)
        var notificationPendingIntent = PendingIntent.getActivity(
            this, NOTIFICATION_ID,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You have been notified")
            .setContentText("This is your notification")
            .setSmallIcon(R.drawable.ic_confirmation_number_black_24dp)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true) /* This closes the notification when the user taps on it */
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notifyBuilder = getNotificationBuilder()
        notifyBuilder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated")
            /* Note: BigPictureStyle is a subclass of NotificationCompat.Style
            * which provides alternative layouts for notifications
            **/
        )
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(false, false, true)
    }

    private fun cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(true, false, false)
    }

    private fun setNotificationButtonState(isNotifyEnabled: Boolean,
                                           isUpdateEnabled: Boolean,
                                           isCancelEnabled: Boolean) {
        buttonNotify.isEnabled = isNotifyEnabled
        buttonUpdate.isEnabled = isUpdateEnabled
        buttonCancel.isEnabled = isCancelEnabled
    }
}
