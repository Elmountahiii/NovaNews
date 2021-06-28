package com.redgunner.novanews.view.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.redgunner.novanews.R
import kotlinx.coroutines.delay


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {


    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenStarted {

            delay(2000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
        }
    }
}