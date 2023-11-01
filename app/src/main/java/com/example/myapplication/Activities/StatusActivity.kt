package com.example.myapplication.Activities

import Gone
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Adapters.StatusAdapter
import com.example.myapplication.Fragments.ProgressFragment
import com.example.myapplication.Interfaces.StatusClickListener
import com.example.myapplication.Model.StatusItems
import com.example.myapplication.R
import com.example.myapplication.SharePreferenceClass
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.databinding.ActivityStatusBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import isNetworkConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import loadNativeAd
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class StatusActivity : AppCompatActivity() {
    private lateinit var prefManager: SharePreferenceClass

    private lateinit var adapter: StatusAdapter

    private var statusFileName: String? = null

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var dialog : Dialog

    private val binding: ActivityStatusBinding by lazy {
        ActivityStatusBinding.inflate(layoutInflater)
    }

    val STATUS_DIRECTORY = File(
        Environment.getExternalStorageDirectory().toString() +
                File.separator + "WhatsApp/Media/.Statuses"
    )

    private var interstitialAd: InterstitialAd? = null

    val WHATS_DIR_FOR_R = "primary:Android/media/com.whatsapp/WhatsApp/Media/.Statuses/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MobileAds.initialize(this@StatusActivity)

        showNativeAd()

        initializeCustomDialog(this)

        loadInterstitialAd()

        prefManager = SharePreferenceClass(this)
        loadStatus()
        val recyclerview = binding.recyclerviewStatusSaver
        recyclerview.layoutManager = GridLayoutManager(this, 2)

        val data = ArrayList<StatusItems>()
        Log.d("Statuus", "onBindViewHolder: $data")

        adapter = StatusAdapter(data, object : StatusClickListener {
            override fun onImageViewClick(uri: Uri?, title: String) {
                saveStatus(uri, title)
            }
        })
        recyclerview.adapter = adapter

        val back = findViewById<ImageView>(R.id.btn_back_status)
        back.setOnClickListener {
            Back()
        }
//        val howItWork = findViewById<ImageView>(R.id.how_it_work)
//        howItWork.setOnClickListener {
//            HowItWork()
//        }
    }

    private fun showNativeAd() {
        with(binding) {
            if (this@StatusActivity.isNetworkConnected()) {
                this@StatusActivity.loadNativeAd(
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

    private fun saveStatus(uri: Uri?, title: String) {
        if (title.endsWith(".mp4")) {
            statusFileName = "Status " + Calendar.getInstance().timeInMillis + " .mp4"
        } else if (title.endsWith(".jpg")) {
            statusFileName = "Status " + Calendar.getInstance().timeInMillis + " .jpg"
        } else {
            statusFileName = "Status " + Calendar.getInstance().timeInMillis + " .png"
        }

        val appDirectory = getExternalFilesDir(null)
        val statusFile = File(appDirectory, statusFileName!!)

        try {
            val inputStream = contentResolver.openInputStream(uri!!)
            val outputStream = FileOutputStream(statusFile)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: 0 } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            inputStream?.close()
            outputStream.close()

            Toast.makeText(
                this@StatusActivity,
                "Status saved!",
                Toast.LENGTH_SHORT
            ).show()

            if (interstitialAd != null) {

                dialog.show()

                handler.postDelayed(
                    {
                        showInterstitialAd()
                    },5000
                )

            }

            val progressFragment=ProgressFragment()
            Log.d("statuspath", "saveStatus: "+statusFile.absolutePath)
            progressFragment.insertMediaFileIntoGalleryIfSyncEnabled(this@StatusActivity,statusFile.absolutePath,"image/jpg")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@StatusActivity, "Failed to save status", Toast.LENGTH_SHORT).show()
        }


    }

    private fun showInterstitialAd() {
        dialog.dismiss()

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {

            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {

            }

            override fun onAdShowedFullScreenContent() {

            }
        }

        interstitialAd?.show(this)
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

    private fun Back() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

//    private fun HowItWork() {
//        val i = Intent(this, HowItWorkActivity::class.java)
//        startActivity(i)
//    }


    private fun loadStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var uri = ""
            uri = prefManager.getString("WHATSAPP_DIR").toString()
            loadStatusAboveR(Uri.parse(uri))
        } else {
            getStatusBelowR()
        }
    }

    private fun getStatusBelowR() {
        if (STATUS_DIRECTORY.exists()) {
            loadStatusFromStorage(STATUS_DIRECTORY)
        }
    }

    private fun loadStatusFromStorage(filePath: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageTempList: ArrayList<StatusItems> = ArrayList()
            val statusFiles = filePath.listFiles()
            if (statusFiles != null && statusFiles.isNotEmpty()) {
                statusFiles.sort()
                statusFiles.forEach {
                    if (it.name.endsWith(".jpg") || it.name.endsWith(".png")) {
                        imageTempList.add(
                            StatusItems(
                                Uri.parse(it.toString()),
                                it.name,
                                it.absolutePath
                            )
                        )
                    } else if (it.name.endsWith(".mp4")) {
                        imageTempList.add(
                            StatusItems(
                                Uri.parse(it.toString()),
                                it.name,
                                it.absolutePath
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    //  here sent data to adapter
                    adapter.updateData(imageTempList)
                }
            }
        }
    }

    private fun loadStatusAboveR(uri: Uri) {
        prefManager.setString("WHATSAPP_DIR", uri.toString())
        if (uri.toString().isEmpty()) {
            checkPermissionsForUri()
            return
        }
        loadStatusAboveRFromStorage(uri)

    }

    private fun loadStatusAboveRFromStorage(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageTempList: ArrayList<StatusItems> = ArrayList()
            val dir = DocumentFile.fromTreeUri(getApplication(), uri)
            if (dir != null) {
                if (!dir.canRead()) {
                    return@launch
                }
                val fileList = dir.listFiles()
                if (fileList.isNotEmpty()) {
                    fileList.forEach {
                        if (it.name!!.endsWith(".jpg") || it.name!!.endsWith(".png")) {
                            imageTempList.add(
                                StatusItems(
                                    it.uri,
                                    it.name!!,
                                    it.uri.path.toString()
                                )
                            )
                        } else if (it.name!!.endsWith(".mp4")) {
                            imageTempList.add(
                                StatusItems(
                                    it.uri,
                                    it.name!!,
                                    it.uri.path.toString()
                                )
                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        // here sent data to adapter
                        Log.d("stasssss", "Before updateData: ${adapter.mList}")

                        adapter.updateData(imageTempList)

                        Log.d("stasssss", "After updateData: ${adapter.mList}")
                    }
                }

            } else {

            }
        }


    }

    private fun checkPermissionsForUri() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("App needs permission to  access  WhatsApp folder to show and download status")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Allow") { dialog, _ ->
            openDirectory()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun openDirectory() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        intent.putExtra(
            "android.provider.extra.INITIAL_URI",
            DocumentsContract.buildDocumentUri(
                "com.android.externalstorage.documents",
                WHATS_DIR_FOR_R
            )
        )
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var uri: Uri? = null
            if (data != null) {
                uri = data.data
                try {
                    try {
                        contentResolver.takePersistableUriPermission(uri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    loadStatusAboveR(uri!!)
                } catch
                    (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}