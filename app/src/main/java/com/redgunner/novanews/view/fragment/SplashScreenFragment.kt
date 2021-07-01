package com.redgunner.novanews.view.fragment


import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.redgunner.novanews.R


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest = AdRequest.Builder().build()


    override fun onStart() {
        super.onStart()

        setUpInterstitialAd()
    }


    private fun setUpInterstitialAd() {


        InterstitialAd.load(requireContext(), getString(R.string.InterstitialAdID), adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd!!.show(requireActivity())
                    mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())

                        }

                        override fun onAdShowedFullScreenContent() {
                            mInterstitialAd = null
                        }
                    }


                }


            })


    }
}