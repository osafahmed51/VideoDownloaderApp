package com.example.myapplication.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Interfaces.StatusClickListener
import com.example.myapplication.Model.StatusItems
import com.example.myapplication.R

class StatusAdapter (val mList: ArrayList<StatusItems>, private val listener: StatusClickListener) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.status, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statusItem = mList[position]
        Log.d("Statuus", "onBindViewHolder: $statusItem")

        Glide.with(holder.itemView)
            .load(statusItem.uri)
            .centerCrop()
            .into(holder.imageViewOne)

        holder.iconImageView.setOnClickListener {
            listener.onImageViewClick(statusItem.uri, statusItem.title)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<StatusItems>) {
        mList.clear()
        mList.addAll(newData)
        Log.d("oooooo", "updateData: $mList")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageViewOne: ImageView = itemView.findViewById(R.id.status1)
        val iconImageView: ImageView = itemView.findViewById(R.id.downloadButton)
    }
}