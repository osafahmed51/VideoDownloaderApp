package com.example.myapplication.Ads


import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import isAlreadyPurchased
import isNetworkConnected


open class InterstitialAdUpdated {

    var mInterstitialAd: InterstitialAd? = null
        private set

    companion object {
        var flag=false
        @Volatile

        private var instance: InterstitialAdUpdated? = null
        var onCloseCallback: (() -> Unit)? = null
        var counter = 0
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdUpdated().also {
                instance = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadInterstitialAd(context: Context) {
        if (!context.isAlreadyPurchased()) {
            if (context.isNetworkConnected()) {
                context.let {
                    val interId = it.getString(R.string.Interstisial_id_splash)
                    InterstitialAd.load(it,
                        interId,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {

                            override fun onAdFailedToLoad(ad: LoadAdError) {

//                                isInterstitialShown=false
                                if (counter == 2) {
                                    onCloseCallback?.invoke()
                                } else {
                                    counter++
                                    onCloseCallback?.invoke()
                                    loadInterstitialAd(context)
                                    Log.d("loaded_interstitial", "onAdFailedToLoad")
                                }
                            }

                            override fun onAdLoaded(ad: InterstitialAd) {
                                mInterstitialAd = ad
                                mInterstitialAd?.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            onCloseCallback?.invoke()
//                                            isInterstitialShown=false

                                            Log.e("InterstiatialReload", "Reloaded___")
                                        }

                                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                                            isInterstitialShown=false
                                            super.onAdFailedToShowFullScreenContent(p0)
                                            onCloseCallback?.invoke()
                                        }

                                        override fun onAdShowedFullScreenContent() {
//                                            isInterstitialShown=true
                                            super.onAdShowedFullScreenContent()
                                            loadInterstitialAd(context)
                                            Log.d(
                                                "loaded_interstitial",
                                                "onAdShowedFullScreenContent: "
                                            )
                                        }
                                    }
                                Log.e("Interstitial____", "AdLoaded____")
                            }
                        })
                }
            }
        }
    }

    // to show Interstitial Ad Activity reference must be given
    @RequiresApi(Build.VERSION_CODES.M)
    fun showInterstitialAdNew(activity: Activity, onAction: (() -> Unit)? = null) {

        if (mInterstitialAd != null) {
            flag =true
            activity.let {
                mInterstitialAd?.show(it)
            }
            onCloseCallback = {
                onAction?.invoke()
                onCloseCallback = null
                flag =false
            }


        } else {
            flag =false
            loadInterstitialAd(activity)
            Log.d("loaded_interstitial", "showInterstitialAdNew: ")
            onAction?.invoke()
        }

    }

}