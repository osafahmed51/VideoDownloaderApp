package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Interfaces.HistoryClickListener
import com.example.myapplication.Model.LinkEntity
import com.example.myapplication.R

class HistoryAdapter(private val entity: MutableList<LinkEntity>,private var context: Context,private val historyclicklistener : HistoryClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.historylayout,parent,false)
        return HistoryAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {
       return entity.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = entity[position]
        holder.historytextview.text= link.link
        holder.historydeleteview.setOnClickListener{
            val popupMenu = PopupMenu(context,holder.historydeleteview)
            popupMenu.menuInflater.inflate(R.menu.historymenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.deletehistory -> {
                        historyclicklistener.onDelete(link)
                        entity.removeAt(position)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.copylinkhistory -> {
                        historyclicklistener.onCopyLink(link)
                        true
                    }
                    R.id.sharelinkhistory -> {
                        historyclicklistener.onShare(link)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        val historytextview: TextView =itemview.findViewById(R.id.historytextview)
        val historydeleteview : ImageView =itemview.findViewById(R.id.optionicon_history)

    }
}