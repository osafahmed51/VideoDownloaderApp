package com.example.myapplication.Fragments

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import com.example.myapplication.Activities.FeedbackActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.downloader.Progress
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.DownloadService
import com.example.myapplication.Model.BottomMainList
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProgressBinding
import com.example.myapplication.utils.Utils
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.util.Calendar


class progressFragment : Fragment(R.layout.fragment_progress) {
    lateinit var binding : FragmentProgressBinding
    lateinit var path : String
    lateinit var BaseURL : String
    lateinit var BaseURlfirst:String
    var downloadID : Int? = null
     var filename : String?=null
    var filename_intent : String?=null
    lateinit var entrydata: BottomMainList
    var utilss= Utils()
    var videoqueue=ArrayList<String>()
    var downloading:Boolean=false
    val isDownloadPaused : Boolean =false
    var TAG : String = "Muxing"
    var TAG1 : String = "intntfilename"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        binding=FragmentProgressBinding.bind(view)

        PRDownloader.initialize(requireContext())

        path=utilss.getRootDirPath(requireContext())
        Toast.makeText(requireContext(),""+path,Toast.LENGTH_LONG)

        if(arguments !=null)
        {
            filename_intent = arguments?.getString("filename")
            Log.d(TAG1, "onViewCreated: "+filename_intent)
        }



        argurments()
        onCLickListener()








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
            entrydata =
                arguments?.getSerializable(Downloadbottmsheet.Bottom_Sheet_Data_Key) as com.example.myapplication.Model.BottomMainList
            BaseURL = entrydata.BaseURL.toString()
            videoqueue.add(BaseURL)
            downloadfile(
                binding.progressBarDownloadProgress,
                path,
                binding.mbsTextvProgress,
                entrydata.AudioLink,
                filename_intent
            )
        }
        else
        {
            binding.cardvProgressVideo1.visibility=View.INVISIBLE
            binding.RecyclerFragmentProgress.visibility=View.INVISIBLE
            binding.nodownloadsprogress.visibility=View.VISIBLE
        }


    }


    fun downloadfile(progressbar: ProgressBar, path: String, mbstextv: TextView,AudioLink: String?,filename1 : String?) {
        if (!videoqueue.isEmpty() && !downloading) {

            BaseURlfirst = videoqueue.removeAt(0)

            filename = filename1
            Log.d(TAG1, "downloadfile: "+filename)

            val videoCachePath =  File(requireActivity().externalCacheDir, "video")
            val videoFilePath =  File(videoCachePath, filename)

            Glide.with(requireContext()).load(BaseURlfirst).into(binding.downloadImgvProgress)
            binding.videotitleProgress.text = filename

//            val intent = Intent(requireContext(), DownloadService::class.java).apply {
//                putExtra("downloadUrl", BaseURlfirst)
//            }
//            requireContext().startService(intent)

            downloadID = PRDownloader.download(BaseURlfirst, videoCachePath.toString(), filename)
                .build()
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Toast.makeText(requireContext(), "Video Download Completed At $videoCachePath", Toast.LENGTH_SHORT).show()
                        downloading = false

                        downloadAudioFile(AudioLink,path, videoFilePath,progressbar,mbstextv,filename)
                    }
                    override fun onError(error: Error?) {
                        Toast.makeText(requireContext(), "Audio Download Error: ${error?.connectionException}", Toast.LENGTH_LONG).show()

                        // Handle the error as needed, such as retrying the download or showing a message to the user.
                    }

                })

            downloading = true

            binding.closebtnProgress.setOnClickListener {
                PRDownloader.cancel(downloadID)
            }

        }
    }


    @SuppressLint("WrongConstant")
    private fun AudioVideoMuxing(outputFile: String, videoPathFile: File, audioPathFile: File,filename1 : String?) {
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
            .setOnProgressListener(object : OnProgressListener {
                override fun onProgress(progress: Progress) {
                    // getting the progress of download
                    val progressPer = progress.currentBytes * 100 / progress.totalBytes
                    // setting the progress to progressbar
                    progressbar.setProgress(progressPer.toInt())
                    // setting the download percent
                    mbstextv.text = utilss.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                    progressbar.isIndeterminate = false
                }
            })

            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(requireContext(), "Audio Download Completed At $audioCachePath", Toast.LENGTH_SHORT).show()


                    val outputFilePath = path

                    AudioVideoMuxing(outputFilePath,videoFilePath,audioFilePath,filename)

                }
                override fun onError(error: Error?) {
                    Toast.makeText(requireContext(), "Audio Download Error: ${error?.connectionException}", Toast.LENGTH_LONG).show()


                }

            })
    }

}