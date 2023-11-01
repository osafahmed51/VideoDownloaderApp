package com.example.myapplication.Fragments

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.Adapters.bottomSheetAdapter
import com.example.myapplication.Model.BottomMainList
import com.example.myapplication.Model.Entry
import com.example.myapplication.Model.Size
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDownloadbottmsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

private const val TAG = "Downloadbottmsheet"
class Downloadbottmsheet(var listofEntries:ArrayList<Entry>?=null,private var instVideoUrl: String? = null) : BottomSheetDialogFragment(R.layout.fragment_downloadbottmsheet) {


    constructor(facebookVideoList: ArrayList<Entry>) : this() {
        this.listofEntries = facebookVideoList
    }

    constructor(instVideoUrl: String) : this() {
        this.instVideoUrl = instVideoUrl
    }

    lateinit var binding: FragmentDownloadbottmsheetBinding

    var adapter: bottomSheetAdapter? = null

    private var client = OkHttpClient()

    private var videosize = ArrayList<Size>()

    private var mainList = ArrayList<BottomMainList>()

    private var dataclasssvar : BottomMainList?=null

    private var filename : String? = null

    private var loadedImage: Drawable? = null

    private var formattedduration : String = ""

    var newposition : Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentDownloadbottmsheetBinding.bind(view)

        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 3)
        binding.recyclerviewBottomsheet.layoutManager = gridLayoutManager

        if(listofEntries != null)
        {
            handleFBLink()
        }
        else if(instVideoUrl != null)
        {
            Log.d(TAG, instVideoUrl!!)
            handleInstLink()
        }


        onClickListeners()



    }

    private fun handleFBLink() {

        CoroutineScope(Dispatchers.IO).launch {


            listofEntries?.forEach {

                val request = Request.Builder()
                    .url(it.BaseURL.toString())
                    .head()
                    .build()


                try {
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val contentLength = response.header("Content-Length")
                        if (contentLength != null) {
                            val sizeInBytes = contentLength.toInt()
                            val sizeInMegabytes = sizeInBytes / (1024.0 * 1024.0)
                            videosize.add(Size(sizeInMegabytes))
                            Log.d(TAG, videosize.toString())


                        } else {
                            Log.e(TAG, "Content-Length header not available.")
                        }
                    } else {
                        Log.e(TAG, "Request was not successful: ${response.code}")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: try catch error: ${e.toString()}")
                    e.printStackTrace()
                }


                val mediaPlayer = MediaPlayer()

                try {

                    mediaPlayer.setDataSource(listofEntries!![0].BaseURL)

                    mediaPlayer.prepareAsync()

                    mediaPlayer.setOnPreparedListener { mp ->

                        val totalDuration = mp.duration

                        formattedduration = formatDuration(totalDuration)
                        Log.d(TAG, "onViewCreated: videotime $formattedduration")


                        binding.videotime.text = "$formattedduration Min"





                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            withContext(Dispatchers.Main) {


                Glide.with(requireActivity())
                    .load(listofEntries!![0].BaseURL)
                    .into(binding.imgvBottomsheet)


                var maxsize = listofEntries?.let { minOf(it.size, videosize.size) }

                for (i in 0 until maxsize!!) {
                    val entries = listofEntries?.get(i)
                    val sizelist = videosize[i]




                    val merged =
                        BottomMainList(
                            entries?.BaseURL, entries?.QualityLabel,
                            listofEntries?.last()?.BaseURL,sizelist.size)
                    mainList.add(merged)
                    Log.d("last_list", ""+ (listofEntries?.last()?.BaseURL ))

                    binding.idPBLoading.visibility = View.INVISIBLE

                }

                adapter = bottomSheetAdapter(requireActivity(),mainList,bottomSheetAdapter)
                binding.recyclerviewBottomsheet.adapter = adapter
                binding.downloadbtn.setOnClickListener {

                           if(adapter!!.selectedposition != RecyclerView.NO_POSITION)
                           {
                               val bundle = Bundle().apply {
                                   putSerializable(Bottom_Sheet_Data_Key, dataclasssvar)
                                   filename?.let { putString("filename", it) }


                               }
                               Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_LONG).show()
                               val progressFragment = ProgressFragment()
                               progressFragment.arguments = bundle
                               Log.d(TAG, "onClickListeners: "+filename)
                               Log.d(TAG, "onClickListeners: "+bundle)
                               (requireActivity() as MainActivity).setCurrentFragment(progressFragment)
                               dismiss()
                           }
                           else
                           {
                               Toast.makeText(requireContext(),"Please Select Any To Proceed.",Toast.LENGTH_SHORT).show()
                           }

                   }


            }
        }


        filename="VidDown"+URLUtil.guessFileName(listofEntries?.get(0)?.BaseURL,null,null)
        binding.bottomsheetTextvMain.text=filename

    }

    private fun handleInstLink() {

        CoroutineScope(Dispatchers.IO).launch {




                val request = Request.Builder()
                    .url(instVideoUrl.toString())
                    .head()
                    .build()


                try {
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val contentLength = response.header("Content-Length")
                        if (contentLength != null) {
                            val sizeInBytes = contentLength.toInt()
                            val sizeInMegabytes = sizeInBytes / (1024.0 * 1024.0)
                            videosize.add(Size(sizeInMegabytes))
                            Log.d(TAG, videosize.toString())


                        } else {
                            Log.e(TAG, "Content-Length header not available.")
                        }
                    } else {
                        Log.e(TAG, "Request was not successful: ${response.code}")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: try catch error: ${e.toString()}")
                    e.printStackTrace()
                }


                val mediaPlayer = MediaPlayer()

                try {

                    mediaPlayer.setDataSource(instVideoUrl)

                    mediaPlayer.prepareAsync()

                    mediaPlayer.setOnPreparedListener { mp ->

                        val totalDuration = mp.duration

                        formattedduration = formatDuration(totalDuration)
                        Log.d(TAG, "onViewCreated: videotime $formattedduration")


                        binding.videotime.text = "$formattedduration Min"





                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            withContext(Dispatchers.Main) {


                Glide.with(requireActivity())
                    .load(instVideoUrl)
                    .into(binding.imgvBottomsheet)



                val sizelist = videosize[0]
                    val merged =
                        BottomMainList(
                            instVideoUrl, "720",
                            "", sizelist.size
                        )
                    mainList.add(merged)
                    Log.d("last_list", ""+ (listofEntries?.last()?.BaseURL ))

                    binding.idPBLoading.visibility = View.INVISIBLE

                adapter = bottomSheetAdapter(requireActivity(),mainList,bottomSheetAdapter)
                binding.recyclerviewBottomsheet.adapter = adapter
                binding.downloadbtn.setOnClickListener {

                        if(adapter!!.selectedposition != RecyclerView.NO_POSITION)
                        {
                            val bundle = Bundle().apply {
                                putSerializable(Bottom_Sheet_Data_Key, dataclasssvar)
                                filename?.let { putString("filename", it) }
                                putString("inst","inst")

                            }

                            val progressFragment = ProgressFragment()
                            progressFragment.arguments = bundle
                            Log.d(TAG, "onClickListeners: "+filename)
                            Log.d(TAG, "onClickListeners: "+bundle)
                            (requireActivity() as MainActivity).setCurrentFragment(progressFragment)
                            dismiss()

                        }
                        else
                        {

                            Toast.makeText(requireContext(),"Please Select Any To Proceed.",Toast.LENGTH_LONG).show()
                        }
                    }



            }
        }


        filename="VidDown"+URLUtil.guessFileName(instVideoUrl,null,null)
        binding.bottomsheetTextvMain.text=filename


    }

    private fun onClickListeners() {

        binding.renamebtn.setOnClickListener {
            showrenmedialog()
        }

        binding.cancelbtn.setOnClickListener {
            dismiss()
        }
    }

    private fun showrenmedialog() {
        val dialogView = View.inflate(requireContext(), R.layout.dialogrename, null)
        val editTextNewName = dialogView.findViewById<EditText>(R.id.editTextNewName)
        editTextNewName.setText(filename)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Rename") { dialog, _ ->
                val newName = editTextNewName.text.toString().trim()
                if (newName.isNotEmpty()) {

                    val updatedName = if (newName.endsWith(".mp4")) {
                        newName
                    } else {
                        "$newName.mp4"
                    }
                    binding.bottomsheetTextvMain.text = updatedName
                    filename = updatedName

                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    val bottomSheetAdapter = object : bottomSheetAdapter.OnClickListener {
        override fun onClick( model: BottomMainList) {
            dataclasssvar = model
        }

    }

    companion object {
        val Bottom_Sheet_Data_Key = "bottomsheetdata"
    }


    override fun onResume() {
        super.onResume()
//        binding.idPBLoading.visibility=View.INVISIBLE
    }

    private fun formatDuration(durationMillis: Int): String {
        val seconds = durationMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val remainingSeconds = seconds % 60
        val remainingMinutes = minutes % 60
        return String.format("%02d:%02d", remainingMinutes, remainingSeconds)
    }
}
