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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
import kotlin.math.log

private const val TAG = "Downloadbottmsheet"
class Downloadbottmsheet(var listofEntries:ArrayList<Entry>) : BottomSheetDialogFragment(R.layout.fragment_downloadbottmsheet) {

    lateinit var binding: FragmentDownloadbottmsheetBinding

    var adapter: bottomSheetAdapter? = null

    private var client = OkHttpClient()

    private var videosize = ArrayList<Size>()

    private var mainList = ArrayList<BottomMainList>()

    private var dataclasssvar : BottomMainList?=null

    private var filename : String? = null

    private var formattedduration : String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadbottmsheetBinding.bind(view)

        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 3)
        binding.recyclerviewBottomsheet.layoutManager = gridLayoutManager
        binding.idPBLoading.visibility=View.GONE


        CoroutineScope(Dispatchers.IO).launch {


            listofEntries.forEach {

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


            }

            withContext(Dispatchers.Main) {


                val mediaPlayer = MediaPlayer()

                try {

                    mediaPlayer.setDataSource(listofEntries[0].BaseURL)

                    mediaPlayer.prepareAsync()

                    mediaPlayer.setOnPreparedListener { mp ->

                        val totalDuration = mp.duration

                        formattedduration = formatDuration(totalDuration)
                        Log.d(TAG, "onViewCreated: videotime $formattedduration")



                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }


                var maxsize = minOf(listofEntries.size, videosize.size)

                for (i in 0 until maxsize) {
                    val entries = listofEntries[i]
                    val sizelist = videosize[i]




                    val merged =
                        BottomMainList(entries.BaseURL, entries.QualityLabel,
                            listofEntries.last().BaseURL,sizelist.size)
                    mainList.add(merged)
                    Log.d("last_list", ""+listofEntries.last().BaseURL)

                }

                adapter = bottomSheetAdapter(requireContext(),mainList,bottomSheetAdapter)
                binding.idPBLoading.visibility=View.VISIBLE
                binding.recyclerviewBottomsheet.adapter = adapter

            }
        }


        Glide.with(requireContext()).load(listofEntries[0].BaseURL).listener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                binding.idPBLoading.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                binding.idPBLoading.visibility = View.GONE
                return false
            }
        }).into(binding.imgvBottomsheet)
        binding.videotime.text=formattedduration
        filename="VidDown"+URLUtil.guessFileName(listofEntries[0].BaseURL,null,null)
        binding.bottomsheetTextvMain.text=filename
        binding.idPBLoading.visibility=View.INVISIBLE


        onClickListeners()



    }

    private fun onClickListeners() {

        binding.renamebtn.setOnClickListener {
            showrenmedialog()
        }

        binding.downloadbtn.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(Bottom_Sheet_Data_Key, dataclasssvar)
                filename?.let { putString("filename", it) }
            }
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_LONG).show()
            val progressFragment = progressFragment()
            progressFragment.arguments = bundle
            Log.d(TAG, "onClickListeners: "+filename)
            (requireActivity() as MainActivity).setCurrentFragment(progressFragment)
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
                    binding.bottomsheetTextvMain.text = newName
                    filename=newName
                } else {

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
        override fun onClick(position: Int, model: BottomMainList) {
            Log.d("ab_click", "onBindViewHolder: on click in fragn")
            dataclasssvar = model
        }

    }

    companion object {
        val Bottom_Sheet_Data_Key = "bottomsheetdata"
    }


    override fun onResume() {
        super.onResume()

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
