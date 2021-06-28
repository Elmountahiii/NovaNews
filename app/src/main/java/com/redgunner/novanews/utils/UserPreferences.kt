package com.redgunner.novanews.utils

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import com.redgunner.novanews.utils.Constants.Companion.theme_Key
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences  @Inject constructor (@ApplicationContext context: Context) {



    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "UserPreferences")

    val themePreferences: Flow<Boolean?>
        get()=dataStore.data.map { Preferences ->
            Preferences[theme_Key]
        }


    suspend fun saveUserThemePreferences(isDark:Boolean){

        dataStore.edit { Preferences ->
            Preferences[theme_Key] = isDark
        }

    }
}