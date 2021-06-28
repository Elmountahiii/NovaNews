package com.redgunner.novanews.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.redgunner.novanews.R
import com.redgunner.novanews.utils.Constants

import com.redgunner.novanews.view.activity.MainActivity

class PushMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {


            Log.d("testa", "notification :${remoteMessage.data}")
            createNotification(
                title = remoteMessage.data["title"]!!,
                content = remoteMessage.data["message"]!!,
                postId = remoteMessage.data["post_id"]!!
            )


        }

    }


    private fun createNotification(title: String, content: String,postId:String) {

        val showActivityIntent = Intent(this, MainActivity::class.java)

        val bundle = Bundle()
        bundle.putString("post_id",postId)
        showActivityIntent.putExtras(bundle)

        val pendingIntent = PendingIntent.getActivity(
            this,
            100,
            showActivityIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )


        val notificationBuilder = NotificationCompat
            .Builder(this, Constants.newsChannelID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this))
        {
            notify(Constants.notificationID, notificationBuilder.build())
        }


    }
}
