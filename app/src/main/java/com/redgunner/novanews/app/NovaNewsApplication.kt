package com.redgunner.novanews.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import com.redgunner.novanews.utils.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NovaNewsApplication:Application()
{
    override fun onCreate() {
        super.onCreate()



        createNotificationChannel()

    }



    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            val channel = NotificationChannel(
                Constants.newsChannelID,
                Constants.newsChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {

                description = Constants.newsChannelDescription
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }





}