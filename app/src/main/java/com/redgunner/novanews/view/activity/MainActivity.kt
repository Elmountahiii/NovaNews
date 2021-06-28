package com.redgunner.novanews.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.redgunner.novanews.R
import com.redgunner.novanews.utils.UserPreferences
import com.redgunner.novanews.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    @Inject
    lateinit var userPreferences: UserPreferences


    override fun onStart() {
        super.onStart()


        themePreferences()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpFirebase()
        val bundle=intent.extras
        if (bundle!=null){
            findNavController(R.id.NevHostFragment).navigate(R.id.detailFragment,bundle)
        }

    }

    override fun onResume() {
        super.onResume()


    }


    private fun themePreferences() {
        userPreferences.themePreferences.asLiveData().observe(this, { isDarak ->
            if (isDarak == true) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


            } else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


            }

        })

    }


    private fun setUpFirebase(){
        FirebaseMessaging.getInstance().subscribeToTopic("post")

    }
}