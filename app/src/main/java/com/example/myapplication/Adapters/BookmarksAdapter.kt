package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Interfaces.BookmarkClickListener
import com.example.myapplication.Model.BookmarkEntity
import com.example.myapplication.R

class BookmarksAdapter(private val bookmarks: MutableList<BookmarkEntity>, private var context: Context, private val bookmarkclicklistener : BookmarkClickListener) : RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder>() {

    inner class BookmarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkItemTextView: TextView = itemView.findViewById(R.id.bookmarkstextview)
        val bookmarkdeleteview: ImageView = itemView.findViewById(R.id.optionicon_bookmarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.bookmarkslayout, parent, false)
        return BookmarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.bookmarkItemTextView.text = bookmark.link
        holder.bookmarkdeleteview.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.bookmarkdeleteview)
            popupMenu.menuInflater.inflate(R.menu.bookmarkmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deletebookmark -> {
                        bookmarkclicklistener.onDelete(bookmark)
                        bookmarks.removeAt(position)
                        notifyDataSetChanged()
                        true
                    }

                    R.id.copylinkbookmark -> {
                        bookmarkclicklistener.onCopyLink(bookmark)
                        true
                    }

                    R.id.sharelinkbookmark -> {
                        bookmarkclicklistener.onShare(bookmark)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }


    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

}
