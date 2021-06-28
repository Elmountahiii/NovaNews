package com.redgunner.novanews.utils

import androidx.datastore.preferences.preferencesKey

class Constants {


    companion object{

        val theme_Key= preferencesKey<Boolean>("Theme_Key")

        const val newsChannelID = "1"

        const val newsChannelName = "News"
        const val newsChannelDescription = "news channel"


        const val notificationID = 0


    }
}