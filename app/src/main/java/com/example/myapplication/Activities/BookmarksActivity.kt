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
import com.example.myapplication.Adapters.BookmarksAdapter
import com.example.myapplication.utils.AppDataBase
import com.example.myapplication.Interfaces.BookmarkClickListener
import com.example.myapplication.Model.BookmarkEntity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBookmarksBinding
import isNetworkConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loadNativeAd

class BookmarksActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookmarksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)


        showNativeAd()

        showDataInRecyclerView()

        onClickListener()

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
        binding.deleteiconbookmarks.setOnClickListener {

            val database by lazy {
                Room.databaseBuilder(this, AppDataBase::class.java, "app_database")
                    .build()

            }
            val linkDao = database.bookmarkDao()

            CoroutineScope(Dispatchers.IO).launch {
                linkDao.deleteAll()


            }

            val linkadapter = BookmarksAdapter(ArrayList(), this@BookmarksActivity, bookmarkonClick)

            runOnUiThread {
                linkadapter.notifyDataSetChanged()
            }


        }
    }

        private fun showDataInRecyclerView() {
            val database by lazy {
                Room.databaseBuilder(this, AppDataBase::class.java, "app_database")
                    .build()
            }

            val linkDaoBookmark = database.bookmarkDao()

            CoroutineScope(Dispatchers.Main).launch {
                val linksdata = linkDaoBookmark.getAllUsers()
                Log.d("linksdatabookmark", "onPageFinished: " + linksdata)

                val linkadapter =
                    BookmarksAdapter(ArrayList(linksdata), this@BookmarksActivity, bookmarkonClick)

                binding.bookmarksrecyclerview.adapter = linkadapter
                binding.bookmarksrecyclerview.layoutManager =
                    LinearLayoutManager(this@BookmarksActivity)

            }

        }

        val bookmarkonClick = object : BookmarkClickListener {
            override fun onDelete(bookmarksentity: BookmarkEntity) {
                val database by lazy {
                    Room.databaseBuilder(
                        this@BookmarksActivity,
                        AppDataBase::class.java,
                        "app_database"
                    )
                        .build()
                }

                val linkDao = database.bookmarkDao()

                CoroutineScope(Dispatchers.Main).launch {

                    Log.d("LinkID", "onDelete: " + bookmarksentity.id)
                    linkDao.deleteUserById(bookmarksentity.id)

                }
            }

            override fun onCopyLink(bookmarksentity: BookmarkEntity) {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Link", bookmarksentity.link)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(
                    this@BookmarksActivity,
                    "Link copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onShare(bookmarksentity: BookmarkEntity) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, bookmarksentity.link)
                startActivity(Intent.createChooser(shareIntent, "Share Link"))
            }
        }
}
