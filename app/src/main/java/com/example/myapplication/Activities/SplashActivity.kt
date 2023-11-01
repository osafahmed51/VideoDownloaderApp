package com.example.myapplication.Activities

import Gone
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySplashBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import isNetworkConnected
import loadNativeAd


class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    lateinit var binding : ActivitySplashBinding

    private var interstitialAd: InterstitialAd? = null

    private lateinit var dialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this@SplashActivity)

        initializeCustomDialog(this@SplashActivity)

        showNativeAd()

        loadInterstitialAd()

        onClickListener()

        hideProgressDialogAfterDelay()


    }

     private fun initializeCustomDialog(context : Context) {

        dialog=Dialog(context)
        dialog.setContentView(R.layout.customdialogbox)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val params = dialog.window?.attributes
        params?.gravity = Gravity.TOP
         val scale = resources.displayMetrics.density
        params?.y = (300 * scale + 0.5f).toInt()
        dialog.window?.attributes = params


    }


    private fun showNativeAd() {
        with(binding) {
            if (isNetworkConnected()) {
                loadNativeAd(
                    frame.adContainer,
                    frame.adShimmerLayout,
                    frame.adFrameLayout,
                    com.example.myapplication.R.layout.large_native_ad,
                    getString(R.string.native_id)
                )
            }else {
                frame1.Gone()
            }

        }
    }


    private fun onClickListener() {

        binding.nextbutton.setOnClickListener{
            if (interstitialAd != null) {

                dialog.show()

                handler.postDelayed(
                    {
                        showInterstitialAd()
                    },5000
                )

            } else {
                val intent = Intent(this@SplashActivity, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun loadInterstitialAd() {

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {

                interstitialAd = ad


            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                interstitialAd = null


            }
        })
    }


    private fun showInterstitialAd() {

        dialog.dismiss()

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {

                val intent = Intent(this@SplashActivity, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                val intent = Intent(this@SplashActivity, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }

            override fun onAdShowedFullScreenContent() {

            }
        }

        interstitialAd?.show(this)
    }

    private fun hideProgressDialogAfterDelay() {
        handler.postDelayed({
            binding.progressbarSplash.visibility = View.GONE
            binding.nextbutton.visibility = View.VISIBLE
        }, 5000)
    }


}