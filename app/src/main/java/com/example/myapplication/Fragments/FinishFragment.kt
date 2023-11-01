package com.example.myapplication.Fragments

import Gone
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.Adapters.FinishAdapter
import com.example.myapplication.Interfaces.DownloadFragItemClickListener
import com.example.myapplication.Model.VideoInfo
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDownloadBinding
import isNetworkConnected
import loadNativeAd
import java.io.File

class FinishFragment : Fragment(R.layout.fragment_download) {

    lateinit var binding: FragmentDownloadBinding

    val retriever = MediaMetadataRetriever()

    var adapter: FinishAdapter? = null

    val videoFiles = mutableListOf<File>()

    val videoInfoList = mutableListOf<VideoInfo>()

    var deleteIconClicked : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDownloadBinding.bind(view)

        binding.recyclerDownloadfrag.layoutManager = LinearLayoutManager(requireContext())

        showNativeAd()

        getVideoFilesFromStorage()

        addingDataToList()

        onClickListener()

        adapter = FinishAdapter(videoInfoList, requireContext(),downfragonclick)
        binding.recyclerDownloadfrag.adapter = adapter
        adapter!!.notifyDataSetChanged()


        if(videoInfoList.isEmpty())
        {
            binding.nodownloadsfinish.visibility=View.VISIBLE
            binding.nodownloadsfinishtextv.visibility=View.VISIBLE
            adapter!!.notifyDataSetChanged()
        }
        else
        {
            binding.nodownloadsfinish.visibility=View.INVISIBLE
            binding.nodownloadsfinishtextv.visibility=View.INVISIBLE
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




    val downfragonclick = object : DownloadFragItemClickListener {
        override fun onPlayClick(videoInfo: VideoInfo) {



            val bundle = Bundle().apply {
                putSerializable(Downloadbottmsheet.Bottom_Sheet_Data_Key, videoInfo)
            }
            val videplayerfrag = VideoPlayer()
            videplayerfrag.arguments=bundle
            (requireActivity() as MainActivity).setCurrentFragment(videplayerfrag)
        }

        override fun onDeleteClick(videoInfo: VideoInfo) {
            val videoFile=File(videoInfo.filePath)
            if(videoFile.exists())
            {
                if(videoFile.delete())
                {
                    videoInfoList.remove(videoInfo)

                }
            }
            adapter?.notifyDataSetChanged()
        }

        override fun onShareClick(videoInfo: VideoInfo) {
            val videoFile = File(videoInfo.filePath)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "video/*"
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                videoFile
            )

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

            startActivity(Intent.createChooser(shareIntent, "Share Video"))
        }

    }

    private fun deleteselecteditems()
    {
        val selectedVideoInfoList=videoInfoList.filter { it.isSelected }
        for(infoVideo in selectedVideoInfoList)
        {
            val videoFilee=File(infoVideo.filePath)
            if(videoFilee.delete())
            {
                videoInfoList.remove(infoVideo)
            }
            else{
                Log.d(TAG, "Failed to delete file: ${videoFilee.absolutePath}")
            }
        }

        selectedVideoInfoList.forEach { it.isSelected = false }

        adapter?.notifyDataSetChanged()

    }





    private fun onClickListener() {

        binding.deleteIcondownload.setOnClickListener {
            binding.deleteallIcondownload.visibility=View.VISIBLE
            binding.selectallIcondownload.visibility=View.VISIBLE
            binding.deleteIcondownload.visibility=View.INVISIBLE
            deleteIconClicked=true
            adapter?.deleteIconClicked()
            val callback = object : OnBackPressedCallback(
                true
            ) {
                override fun handleOnBackPressed() {
                    binding.deleteallIcondownload.visibility=View.INVISIBLE
                    binding.selectallIcondownload.visibility=View.INVISIBLE
                    binding.deleteIcondownload.visibility=View.VISIBLE
                    deleteIconClicked=false


                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner, // LifecycleOwner
                callback
            )


        }
        binding.selectallIcondownload.setOnClickListener {
            adapter?.checkboxSelectAll()
        }
        binding.deleteallIcondownload.setOnClickListener {
//            deleteDataFromStorage()
            deleteselecteditems()
        }

        if(deleteIconClicked==false)
        {
            onBackPress()
        }


    }

    private fun onBackPress() {
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

    private fun addingDataToList() {
        videoInfoList.clear()
        for (videoFile in videoFiles) {
            val videoFilePath = videoFile.absolutePath

            if (videoFilePath.isNotEmpty()) {
                val videoName = videoFile.name
                val videoSize = videoFile.length()
                val isImage=videoName.endsWith(".jpg",  true)
                if(isImage)
                {

                    val formattedVideoSize = formatFileSize(videoSize)
                    videoInfoList.add(VideoInfo(videoFilePath, formattedVideoSize, "", "", videoName,false,false,true))

                    retriever.release()
                }
                else
                {
                    val formattedVideoSize = formatFileSize(videoSize)
                    videoInfoList.add(VideoInfo(videoFilePath, formattedVideoSize, "", "", videoName,false,false,false))

                    retriever.release()
                }

            }
        }
    }

    private fun getVideoFilesFromStorage() : List<File> {
        val externalStorageDirs =
            ContextCompat.getExternalFilesDirs(requireContext().applicationContext, null)

        if (externalStorageDirs.isNotEmpty()) {
            val externalStorageDir = externalStorageDirs[0]

            val filesInDirectory = externalStorageDir.listFiles()
            if (filesInDirectory != null) {
                videoFiles.addAll(filesInDirectory.filter { it.isFile })
            }
        }
        return videoFiles
    }



    private fun formatFileSize(videoSize: Long): Any {

        val sizeInMB = videoSize / (1024.0 * 1024.0)
        return sizeInMB

    }

    companion object
    {
        private const val TAG = "DownloadFragment"
    }



    override fun onResume() {
        super.onResume()

        videoFiles.clear()
        videoInfoList.clear()

        getVideoFilesFromStorage()

        addingDataToList()

        adapter = FinishAdapter(videoInfoList, requireContext(),downfragonclick)
        binding.recyclerDownloadfrag.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }
}