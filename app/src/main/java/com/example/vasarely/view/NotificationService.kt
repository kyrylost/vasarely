package com.example.vasarely.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.vasarely.R
import com.example.vasarely.model.user.FollowersAmountChangeListener


class NotificationService : Service() {

    lateinit var channelId: String
    //lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        //notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createNotificationChannel("my_service", "My Background Service")
            else ""

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message) as String + " " + "test")
            .setSmallIcon(R.drawable.logo)
            .build()

        startForeground(1, notification)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val extras = intent!!.extras
        val uid = extras!!.get("Uid").toString()
        val followersAmountChangeListener = FollowersAmountChangeListener(uid)

        followersAmountChangeListener.followersAmountLiveData.observeForever {

            Log.d("ofqewdoqwidjwqiodFALD", "qejop")

            val pendingIntent: PendingIntent =
                Intent(this, SearchScreen::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(
                        this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

            val notification: Notification = Notification.Builder(this, channelId)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message) as String + " " + it)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(2, notification)
        }

        return super.onStartCommand(intent, flags, startId)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}