package com.example.myapplication.Activities

import Gone
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.myapplication.Fragments.Tabfragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityExitBinding
import com.example.myapplication.databinding.ActivityPrivacyPolicyBinding
import com.google.android.gms.ads.MobileAds
import isNetworkConnected
import loadNativeAd
import kotlin.system.exitProcess

class ExitActivity : AppCompatActivity() {

    lateinit var binding : ActivityExitBinding

    val tabfrag = Tabfragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityExitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this@ExitActivity)

        showNativeAd()

        onClickListener()



    }
    private fun onClickListener() {

        binding.submitbtnexit.setOnClickListener {
            val rating = binding.ratingBar.rating
            if (rating.toInt() == 5) {
                openPlayStoreRating(rating.toInt())
            }
            else if(rating.toInt() <= 4) {
               sendfeedBack()
            }
            else
            {

            }
        }

        binding.Exitbtn.setOnClickListener{
            this.finishAffinity()
            exitProcess(0)
        }

        binding.trystoriesfeature.setOnClickListener {
            val intentstatus=Intent(this,StatusActivity::class.java)
            startActivity(intentstatus)
        }

        binding.trywallpaperimgv.setOnClickListener {
            val intentwallpaper=Intent(this,MainActivity::class.java)
            startActivity(intentwallpaper)
        }
        binding.tryinstaimgv.setOnClickListener {
            val intentinsta=Intent(this,MainActivity::class.java)
            startActivity(intentinsta)
        }


    }


    private fun openPlayStoreRating(rating: Int) {
        val packageName = packageName

        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (rating > 0) {
                intent.putExtra("rating", rating)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun sendfeedBack() {

        val recipientEmail = "osafahmad51@gmail.com"
        val emailSubject = "Feedback from the App"

        val feedbackMessage = "User is not satisfied with the app."

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$recipientEmail")
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, feedbackMessage)

        try {
            startActivity(Intent.createChooser(intent, "Send Feedback"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email app found to send feedback.", Toast.LENGTH_SHORT).show()
        }

    }




    private fun showNativeAd() {

        with(binding) {
            if (isNetworkConnected()) {
                loadNativeAd(
                    frame.adContainer,
                    frame.adShimmerLayout,
                    frame.adFrameLayout,
                    com.example.myapplication.R.layout.large_native_ad,
                    getString(com.example.myapplication.R.string.native_id)
                )
            }else {
                frame1.Gone()
            }

        }
    }

}