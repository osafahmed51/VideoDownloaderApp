package com.example.myapplication.Activities

import Gone
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplication.Adapters.HistoryAdapter
import com.example.myapplication.utils.AppDataBase
import com.example.myapplication.Interfaces.HistoryClickListener
import com.example.myapplication.Model.LinkEntity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHistoryClassBinding
import isNetworkConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loadNativeAd

class HistoryClass : AppCompatActivity() {

    lateinit var binding : ActivityHistoryClassBinding

    val database by lazy {
        Room.databaseBuilder(this, AppDataBase::class.java,"app_database")
            .build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHistoryClassBinding.inflate(layoutInflater)
        setContentView(binding.root)


        showDataInRecyclerView()

        onClickListener()

        showNativeAd()


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

    private fun onClickListener() {
        binding.deleteiconhistory.setOnClickListener{

            val linkDao =database.linkDao()

            CoroutineScope(Dispatchers.IO).launch {
                linkDao.deleteAll()
            }

            val linkadapter = HistoryAdapter(ArrayList(),this@HistoryClass,historyonClick)
            runOnUiThread {
                linkadapter.notifyDataSetChanged()
            }

        }
    }

    private fun showDataInRecyclerView() {

        val linkDao = database.linkDao()

        CoroutineScope(Dispatchers.Main).launch {
            val linksdata = linkDao.getAllUsers()
            Log.d("linksdata", "onPageFinished: "+linksdata)

            val linkadapter = HistoryAdapter(ArrayList(linksdata),this@HistoryClass,historyonClick)

            binding.recyclerviewHistory.adapter=linkadapter
            binding.recyclerviewHistory.layoutManager= LinearLayoutManager(this@HistoryClass)
        }

    }

    val historyonClick = object : HistoryClickListener {
        override fun onDelete(linksentity: LinkEntity) {

            val linkDao = database.linkDao()

            CoroutineScope(Dispatchers.Main).launch {

                Log.d("LinkID", "onDelete: "+linksentity.id)
                linkDao.deleteUserById(linksentity.id)

                }
            }

        override fun onCopyLink(linksentity: LinkEntity) {

            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Link", linksentity.link)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(this@HistoryClass, "Link copied to clipboard", Toast.LENGTH_SHORT).show()



        }

        override fun onShare(linksentity: LinkEntity) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linksentity.link)
            startActivity(Intent.createChooser(shareIntent, "Share Link"))

        }
    }



    }
