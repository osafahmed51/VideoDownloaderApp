package com.example.myapplication.Fragments

import Gone
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import com.example.myapplication.Activities.FeedbackActivity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.PRDownloader
import com.downloader.Progress
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.Adapters.progressDownloadAdapter
import com.example.myapplication.DownloadService
import com.example.myapplication.Model.BottomMainList
import com.example.myapplication.R
import com.example.myapplication.SharePreferenceClass
import com.example.myapplication.databinding.FragmentProgressBinding
import com.example.myapplication.utils.Utils
import com.google.android.gms.ads.AdRequest
import isNetworkConnected
import loadNativeAd
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer


class ProgressFragment : Fragment(R.layout.fragment_progress) {
    lateinit var binding : FragmentProgressBinding

    lateinit var path : String

    lateinit var BaseURL : String

    lateinit var BaseURlfirst:String

    private var downloadID : Int? = null

     var filename : String?=null

    var filename_intent : String?=null

    var instcheck : String?=null

    lateinit var entrydata: BottomMainList

    var adapter: progressDownloadAdapter? = null

    var utilss= Utils()

    var videoqueue=ArrayList<String>()

    var downloading:Boolean=false

    var isDownloadPaused : Boolean =false

    var TAG : String = "Muxing"

    var TAG1 : String = "intntfilename"

    private lateinit var videoCachePath : File


    private val handler = Handler(Looper.getMainLooper())



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding=FragmentProgressBinding.bind(view)

        videoCachePath=File(requireActivity().externalCacheDir, "video")

        showNativeAd()


        PRDownloader.initialize(requireContext())

        path=utilss.getRootDirPath(requireContext())

        argurments()

        onCLickListener()

        if (!downloading) {
            binding.nodownloadsprogress.visibility = View.VISIBLE
            binding.nodownloadsprogresstextv.visibility=View.VISIBLE
        } else {
            binding.nodownloadsprogress.visibility = View.GONE
            binding.nodownloadsprogresstextv.visibility=View.GONE
        }

    }


    private fun showNativeAd() {
        with(binding) {
            if (requireContext().isNetworkConnected()) {
                requireContext().loadNativeAd(
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
    private fun onCLickListener() {


        binding.progressimgv2.setOnClickListener {

            val popupMenu = PopupMenu(requireContext(),binding.progressimgv2)

            popupMenu.menuInflater.inflate(R.menu.progress_option_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.pauseall -> {

                        true
                    }
                    R.id.resumeall -> {

                        true
                    }
                    R.id.batchdelete -> {

                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }




        binding.progressfeedback.setOnClickListener {
            val intent = Intent(requireActivity(), FeedbackActivity::class.java)
            startActivity(intent)

        }

        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {

                val tabfragment = Tabfragment()

                (requireActivity() as MainActivity).setCurrentFragment(tabfragment)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )






    }

    private fun argurments()
    {


        if(arguments != null) {

            entrydata = (arguments?.getSerializable(Downloadbottmsheet.Bottom_Sheet_Data_Key) as? BottomMainList)!!

                BaseURL = entrydata.BaseURL.toString()

                videoqueue.add(BaseURL)

                filename_intent = arguments?.getString("filename")

                instcheck = arguments?.getString("inst")

                Log.d(TAG1, "onViewCreated: $filename_intent")

            if(instcheck.equals("inst"))
            {
                downloadfile(

                    path,
                    binding.progressBarDownloadProgress,
                    path,
                    binding.mbsTextvProgress,
                    entrydata.AudioLink,
                    filename_intent
                )
            }
            else{
                downloadfile(

                    videoCachePath,
                    binding.progressBarDownloadProgress,
                    path,
                    binding.mbsTextvProgress,
                    entrydata.AudioLink,
                    filename_intent
                )
            }



        }
        else
        {
            binding.cardvProgressVideo1.visibility=View.INVISIBLE

        }


    }


    private fun downloadfile(filecachepath : String?=null, progressbar: ProgressBar, path: String, mbstextv: TextView, AudioLink: String?, filename1 : String?) {
        if (!videoqueue.isEmpty() && !downloading) {

            BaseURlfirst = videoqueue.removeAt(0)

            filename = filename1
            Log.d(TAG1, "downloadfile: "+filename)
            Log.d("PATH", "downloadfile: "+path)

            Glide.with(requireActivity())
                .load(BaseURlfirst)
                .into(binding.downloadImgvProgress)

            val videoFilePath =  File(path, filename)

            binding.videotitleProgress.text = filename

            val intent = Intent(requireContext(), DownloadService::class.java).apply {
                putExtra("downloadUrl", BaseURlfirst)
            }
            requireContext().startService(intent)

            downloadID = PRDownloader.download(BaseURlfirst, filecachepath.toString(), filename)
                .build()
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress) {

                        val progressPer = progress.currentBytes * 100 / progress.totalBytes

                        progressbar.setProgress(progressPer.toInt())

                        mbstextv.text = utilss.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        progressbar.isIndeterminate = false
                    }
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        downloading = false


                        insertMediaFileIntoGalleryIfSyncEnabled(requireContext(), "$path/$filename","video/mp4")



                        binding.cardvProgressVideo1.visibility=View.GONE

                    }
                    override fun onError(error: Error?) {


                    }

                })

            binding.pauseimgvProgress.setOnClickListener {
                if (isDownloadPaused) {

                    PRDownloader.resume(downloadID!!)
                    isDownloadPaused = false

                    binding.pauseimgvProgress.setImageResource(R.drawable.pauseicon)
                } else {
                    PRDownloader.pause(downloadID!!)
                    isDownloadPaused = true

                    binding.pauseimgvProgress.setImageResource(R.drawable.resumeicon)
                }
            }

            binding.closebtnProgress.setOnClickListener {

                    PRDownloader.cancel(downloadID!!)


            }

            downloading = true



        }
    }
    private fun downloadfile(filecachepath : File, progressbar: ProgressBar, path: String, mbstextv: TextView, AudioLink: String?, filename1 : String?) {
        if (!videoqueue.isEmpty() && !downloading) {

            BaseURlfirst = videoqueue.removeAt(0)

            filename = filename1
            Log.d(TAG1, "downloadfile: "+filename)

            Glide.with(requireActivity())
                .load(BaseURlfirst)
                .into(binding.downloadImgvProgress)

            val videoFilePath =  File(filecachepath, filename)

            binding.videotitleProgress.text = filename

            val intent = Intent(requireContext(), DownloadService::class.java).apply {
                putExtra("downloadUrl", BaseURlfirst)
            }
            requireContext().startService(intent)

            downloadID = PRDownloader.download(BaseURlfirst, filecachepath.toString(), filename)
                .build()
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress) {

                        val progressPer = progress.currentBytes * 100 / progress.totalBytes

                        progressbar.setProgress(progressPer.toInt())

                        mbstextv.text = utilss.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        progressbar.isIndeterminate = false
                    }
                })

                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {

                        downloading = false

                        downloadAudioFile(AudioLink,path, videoFilePath,progressbar,mbstextv,filename)

                        binding.cardvProgressVideo1.visibility=View.GONE

                        val dialog = AlertDialog.Builder(requireContext())
                            .setTitle("Muxing")
                            .setMessage("Wait...")
                            .setCancelable(false)
                            .create()

                        dialog.show()

                        Handler().postDelayed({
                            dialog.dismiss()
                        }, 4000)



                    }
                    override fun onError(error: Error?) {


                    }

                })

            binding.pauseimgvProgress.setOnClickListener {
                if (isDownloadPaused) {

                    PRDownloader.resume(downloadID!!)
                    isDownloadPaused = false

                    binding.pauseimgvProgress.setImageResource(R.drawable.pauseicon)
                } else {

                    PRDownloader.pause(downloadID!!)
                    isDownloadPaused = true

                    binding.pauseimgvProgress.setImageResource(R.drawable.resumeicon)
                }
            }

            binding.closebtnProgress.setOnClickListener {
                PRDownloader.cancel(downloadID!!)
                binding.cardvProgressVideo1.visibility=View.GONE


            }

            downloading = true




        }
    }


    @SuppressLint("WrongConstant")
    private fun AudioVideoMuxing(
        outputFile: String, videoPathFile: File, audioPathFile: File,
        filename1: String?) {
        try {
            val fileName = filename1
            val output = File(outputFile, fileName)
            Log.d(TAG, "AudioVideoMuxing: output $output")
            Log.d(TAG, "AudioVideoMuxing: Video$videoPathFile")
            Log.d(TAG, "AudioVideoMuxing: Audio$audioPathFile")
            val videoExtractor = MediaExtractor()
            val fileInputStream = FileInputStream(videoPathFile)
            val fileDescriptor = fileInputStream.fd
            videoExtractor.setDataSource(fileDescriptor)
            val audioExtractor = MediaExtractor()
            audioExtractor.setDataSource(audioPathFile.absolutePath)
            Log.d(TAG, "Video Extractor Track Count " + videoExtractor.trackCount)
            Log.d(TAG, "Audio Extractor Track Count " + audioExtractor.trackCount)
            val muxer = MediaMuxer(output.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            videoExtractor.selectTrack(0)
            val videoFormat = videoExtractor.getTrackFormat(0)
            val videoTrack = muxer.addTrack(videoFormat)
            audioExtractor.selectTrack(0)
            val audioFormat = audioExtractor.getTrackFormat(0)
            val audioTrack = muxer.addTrack(audioFormat)
            Log.d(TAG, "Video Format $videoFormat")
            Log.d(TAG, "Audio Format $audioFormat")
            var sawEOS = false
            var frameCount = 0
            val offset = 100
            val sampleSize = 1024 * 1024
            val videoBuf = ByteBuffer.allocate(sampleSize)
            val audioBuf = ByteBuffer.allocate(sampleSize)
            val videoBufferInfo = MediaCodec.BufferInfo()
            val audioBufferInfo = MediaCodec.BufferInfo()
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            muxer.start()
            while (!sawEOS) {
                videoBufferInfo.offset = offset
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset)
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    Log.d(TAG, "saw input EOS.")
                    sawEOS = true
                    videoBufferInfo.size = 0
                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor.sampleTime
                    videoBufferInfo.flags = videoExtractor.sampleFlags
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo)
                    videoExtractor.advance()
                    frameCount++
                    Log.d(
                        TAG,
                        "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024
                    )
                    Log.d(
                        TAG,
                        "Frame (" + frameCount + ") Audio PresentationTimeUs:" + audioBufferInfo.presentationTimeUs + " Flags:" + audioBufferInfo.flags + " Size(KB) " + audioBufferInfo.size / 1024
                    )
                }
            }
            var sawEOS2 = false
            var frameCount2 = 0
            while (!sawEOS2) {
                frameCount2++
                audioBufferInfo.offset = offset
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset)
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    Log.d(TAG, "saw input EOS.")
                    sawEOS2 = true
                    audioBufferInfo.size = 0
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.sampleTime
                    audioBufferInfo.flags = audioExtractor.sampleFlags
                    muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo)
                    audioExtractor.advance()
                    Log.d(
                        TAG,
                        "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024
                    )
                    Log.d(
                        TAG,
                        "Frame (" + frameCount + ") Audio PresentationTimeUs:" + audioBufferInfo.presentationTimeUs + " Flags:" + audioBufferInfo.flags + " Size(KB) " + audioBufferInfo.size / 1024
                    )
                }
            }
            muxer.stop()
            muxer.release()

            Log.d(TAG, "AudioVideoMuxingoutput: "+output)
            insertMediaFileIntoGalleryIfSyncEnabled(requireContext(),output.toString(),"video/mp4")


        }
     catch (e: IOException) {
        Log.d(TAG, "Mixer Error 1 " + e.message)
    }

    }





    fun downloadAudioFile(AudioLink: String?,path: String, videoFilePath: File,progressbar: ProgressBar, mbstextv: TextView,filename1 :String?) {


        val audioFilename = filename1
        Log.d(TAG1, "downloadAudioFile: "+audioFilename)

        val audioCachePath =  File(requireActivity().externalCacheDir, "audio")
        val audioFilePath =  File(audioCachePath, audioFilename)

        PRDownloader.download(AudioLink, audioCachePath.toString(), audioFilename)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val outputFilePath = path


                    AudioVideoMuxing(outputFilePath,videoFilePath,audioFilePath,filename)


                }
                override fun onError(error: Error?) {

                }

            })
    }


    fun insertMediaFileIntoGalleryIfSyncEnabled(context: Context, filePath: String, mimeType: String) {

        val sharedPreferenceClass = SharePreferenceClass(context)
        val isSyncEnabled = sharedPreferenceClass.getSyncGallerySettings(context)

        if (isSyncEnabled) {
            val contentResolver: ContentResolver = context.contentResolver

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "my_video")
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.DATA, filePath)
            }

            val externalUri = when (mimeType) {
                "image/jpg" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video/mp4" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            try {
                val uri: Uri? = contentResolver.insert(externalUri, values)
                if (uri != null) {
                    Log.d("Insertion", "Media file inserted into the gallery: $uri")
                    Log.d("Insertion", "insertMediaFileIntoGallery: " + filePath)
                } else {
                    Log.e("Insertion", "Failed to insert media file into the gallery.")
                }
            } catch (e: Exception) {
                Log.e("Insertion", "Error inserting media file: ${e.message}")
            }
        }
    }





}