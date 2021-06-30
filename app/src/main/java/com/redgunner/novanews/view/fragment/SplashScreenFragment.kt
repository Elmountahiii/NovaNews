package com.redgunner.novanews.view.fragment


import kotlinx.coroutines.launch
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.redgunner.novanews.R
import kotlinx.coroutines.delay


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private lateinit var mInterstitialAd : InterstitialAd



    override fun onStart() {
        super.onStart()

       /* lifecycleScope.launchWhenStarted {

            delay(2000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
        }*/

        setUpInterstitialAd()
    }


private fun setUpInterstitialAd(){

    mInterstitialAd= InterstitialAd(context)
    mInterstitialAd.adUnitId = getString(R.string.InterstitialAdID)


    val adBuilder=AdRequest.Builder().build()
    mInterstitialAd.loadAd(adBuilder)
    mInterstitialAd.adListener= object : AdListener() {

        override fun onAdLoaded() {
            super.onAdLoaded()

            mInterstitialAd.show()


        }

        override fun onAdClosed() {
            super.onAdClosed()
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())


        }
    }
}
}