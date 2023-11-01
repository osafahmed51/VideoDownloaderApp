package com.example.myapplication.Activities

import Gone
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPrivacyPolicyBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import isNetworkConnected
import loadNativeAd

class PrivacyPolicyActivity : AppCompatActivity() {

    lateinit var binding:ActivityPrivacyPolicyBinding

    private var interstitialAd: InterstitialAd? = null

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this@PrivacyPolicyActivity)

        initializeCustomDialog(this@PrivacyPolicyActivity)

        showNativeAd()

        loadInterstitialAd()

        textStyleAndColor()

        onClicklistener()

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
                val intent = Intent(this@PrivacyPolicyActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                val intent = Intent(this@PrivacyPolicyActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun onAdShowedFullScreenContent() {
            }
        }
        interstitialAd?.show(this)
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

    private fun textStyleAndColor() {

        val htmltext=getString(R.string.privacypolicytext1)
        binding.textView12.text= Html.fromHtml(htmltext,Html.FROM_HTML_MODE_LEGACY)
        binding.textView12.setTextIsSelectable(true)


        val colorText=getString(R.string.privacypolicytext2)
        val spanningtext=SpannableString(colorText)

        val start = colorText.indexOf("Privacy Policy")
        val end = start + "Privacy Policy".length

        val redColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val redColorSpan = ForegroundColorSpan(redColor)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val url = "https://www.google.com"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        spanningtext.setSpan(redColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanningtext.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        binding.textView13.text = spanningtext
        binding.textView13.movementMethod = LinkMovementMethod.getInstance()

    }



    private fun onClicklistener() {

        binding.acceptbutton.setOnClickListener{
            if (interstitialAd != null) {
                initializeCustomDialog(this@PrivacyPolicyActivity)
                dialog.show()
                handler.postDelayed({
                    showInterstitialAd()
                                    },5000)
            } else {
                val intent = Intent(this@PrivacyPolicyActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}