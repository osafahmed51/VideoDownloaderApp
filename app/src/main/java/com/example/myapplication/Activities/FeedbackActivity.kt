package com.example.myapplication.Activities

import Gone
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityFeedbackBinding
import isNetworkConnected
import loadNativeAd

class FeedbackActivity : AppCompatActivity() {

    lateinit var binding : ActivityFeedbackBinding

    val recipientEmail = "osafahmad51@gmail.com"

    val emailSubject = "Feedback from the App"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNativeAd()

        sendFeedBack()

        onClickListener()


    }

    private fun onClickListener() {
        binding.imageView2.setOnClickListener {
            val intentbackk=Intent(this@FeedbackActivity,MainActivity::class.java)
            startActivity(intentbackk)
        }
    }

    private fun showNativeAd() {

        with(binding) {
            if (isNetworkConnected()) {
                loadNativeAd(
                    frame.adContainer,
                    frame.ShimmerContainerSmall,
                    frame.FrameLayoutSmall,
                    com.example.myapplication.R.layout.small_native_ad,
                    getString(R.string.native_id)
                )
            }else {
                nativeAd.Gone()
            }

        }

    }

    private fun sendFeedBack() {
            binding.sendbtnfeedback.setOnClickListener {
                val feedbackMessage = StringBuilder()

                if (binding.downloadfailedCheckbox.isChecked) {
                    feedbackMessage.append("Download Failed\n")
                } else if (binding.downloadspeedslow.isChecked) {
                    feedbackMessage.append("Download speed is too slow\n")
                } else if (binding.downloadtimeout.isChecked) {
                    feedbackMessage.append("Download timeout\n")
                } else if (binding.downloadothers.isChecked) {
                    feedbackMessage.append("Other issues\n")
                }

                val additionalFeedback = binding.editTextText2.text.toString().trim()
                if (additionalFeedback.isNotEmpty()) {
                    feedbackMessage.append("Additional Feedback: $additionalFeedback\n")
                }


                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                intent.putExtra(Intent.EXTRA_TEXT, feedbackMessage.toString())


                val emailAppActivities: List<ResolveInfo> = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in emailAppActivities) {
                    val packageName = resolveInfo.activityInfo.packageName
                    if (packageName == "com.google.android.gm") {
                        intent.setPackage(packageName)
                        break
                    }
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {

                }
            }

    }
}