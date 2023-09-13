package com.example.myapplication


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.os.IBinder
import android.util.Log
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.myapplication.utils.Utils

class DownloadService : Service() {

    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "download_channel"
    var utilss= Utils()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("downloadUrl")) {
            val downloadUrl = intent.getStringExtra("downloadUrl")


            PRDownloader.initialize(this)


            val downloadId = PRDownloader.download(downloadUrl, utilss.getRootDirPath(this.applicationContext)
                , "downloaded_file")
                .build()
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {

                        stopSelf()
                    }

                    override fun onError(error: com.downloader.Error?) {

                    }
                })

            showNotification()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val name = "Download Service"
        val descriptionText = "Downloading files"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification() {
        val notificationIntent = Intent(this, DownloadService::class.java)
        Log.d("show_func_called","Show Notification")
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading File")
            .setContentText("File is downloading...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}