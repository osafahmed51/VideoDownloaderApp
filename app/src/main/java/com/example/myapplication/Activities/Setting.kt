package com.example.myapplication.Activities

import Gone
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.myapplication.utils.AppDataBase
import com.example.myapplication.R
import com.example.myapplication.SharePreferenceClass
import com.example.myapplication.databinding.ActivitySettingBinding
import isNetworkConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loadNativeAd
import showAdAndGo
import java.io.File

class Setting : AppCompatActivity() {

    lateinit var binding : ActivitySettingBinding

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        onClickListener()

        setSyncGalleySetting()

        getEngineName()

        showNativeAd()

        val packageInfo = packageManager.getPackageInfo(packageName, 0)

        val versionName = packageInfo.versionName

        binding.version.text = "App Version: $versionName"
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

    private fun setSyncGalleySetting() {


        val sharedPreferenceClass = SharePreferenceClass(this)
        val swtchVal = sharedPreferenceClass.getSyncGallerySettings(this)
        binding.switchGallery.isChecked = swtchVal
        binding.switchGallery.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                sharedPreferenceClass.setSyncGallerySettings(this@Setting,isChecked)
            }
        })


    }

    private fun getEngineName() {
        val sharePreference = SharePreferenceClass(this)
        val selectedOption = sharePreference.getSelectedOption(this)
        binding.engineName.text = selectedOption
    }

    private fun onClickListener() {


        binding.cookies.setOnClickListener {
            alertDialog()
        }

        binding.btnBack.setOnClickListener {
            this.showAdAndGo {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }

        binding.clearHistory.setOnClickListener {
            alertDialogue()
        }

        binding.searchEngine.setOnClickListener {
            SearchEngine()
        }


        binding.clearCache.setOnClickListener {
            clearAppCache(applicationContext)

            Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show()
        }

        binding.howToDownload.setOnClickListener {
            HowToDownload()
        }


        binding.feedBack.setOnClickListener {
            FeedBack()
        }
        binding.appCompatButtonRemoveads.setOnClickListener {
            val intentremoveads=Intent(this@Setting,RemoveAdsActivity::class.java)
            startActivity(intentremoveads)
        }

    }

    private fun alertDialogue() {
        val builder = android.app.AlertDialog.Builder(this)

        builder.setMessage("Are you sure you want to delete it?")

        builder.setCancelable(false)

        builder.setPositiveButton("DELETE") { dialog, which ->
            deleteFunction()
            dialog.dismiss()
            Toast.makeText(this, "History deleted", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteFunction(){

        val database by lazy {
            Room.databaseBuilder(this, AppDataBase::class.java,"app_database")
                .build()
        }
        val linkDao =database.linkDao()
        CoroutineScope(Dispatchers.IO).launch {
            linkDao.deleteAll()
        }
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private fun SearchEngine() {

        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.search_engine, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Select Options")

        val checkboxOption1 = dialogView.findViewById<CheckBox>(R.id.google)
        val checkboxOption2 = dialogView.findViewById<CheckBox>(R.id.bing)
        val checkboxOption3 = dialogView.findViewById<CheckBox>(R.id.yahoo)

        val name = findViewById<TextView>(R.id.engine_name)

        checkboxOption1.isChecked = (name.text == "Google")
        checkboxOption2.isChecked = (name.text == "Bing")
        checkboxOption3.isChecked = (name.text == "Yahoo")

        val dialog = dialogBuilder.create()

        checkboxOption1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                name.text = "Google"
                checkboxOption2.isChecked = false
                checkboxOption3.isChecked = false
                val sharePreference = SharePreferenceClass(this)
                sharePreference.saveSelectedOption("Google")
            } else {
                // Option 1 is unchecked
            }
            dialog.dismiss()
        }

        checkboxOption2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                name.text = "Bing"
                checkboxOption1.isChecked = false
                checkboxOption3.isChecked = false
                val sharePreference = SharePreferenceClass(this)
                sharePreference.saveSelectedOption("Bing")
            } else {
                // Option 2 is unchecked
            }
            dialog.dismiss()
        }

        checkboxOption3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                name.text = "Yahoo"
                checkboxOption1.isChecked = false
                checkboxOption2.isChecked = false
                val sharePreference = SharePreferenceClass(this)
                sharePreference.saveSelectedOption("Yahoo")
            } else {
                // Option 3 is unchecked
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun clearAppCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            val filesDir = context.filesDir

            clearDirectory(cacheDir)

            clearDirectory(filesDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun clearDirectory(directory: File) {
        if (directory.exists()) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        clearDirectory(file)
                    } else {
                        file.delete()
                    }
                }
            }
        }
    }

    private fun HowToDownload(){
        val i = Intent(this, downloadSlider::class.java)
        startActivity(i)
    }

    private fun FeedBack(){
        val i = Intent(this, FeedbackActivity::class.java)
        startActivity(i)
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Clear Cookies")
        builder.setMessage("Are you sure you want to clear cookies?")

        builder.setPositiveButton("Yes") { dialog, which ->
            clearCookies()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun clearCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        Toast.makeText(this, "Cookies cleared", Toast.LENGTH_SHORT).show()
    }
}