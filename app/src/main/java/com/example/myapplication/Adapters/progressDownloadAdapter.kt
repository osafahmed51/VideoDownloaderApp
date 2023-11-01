package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.ProgressFragDataClass
import com.example.myapplication.R

class progressDownloadAdapter(private val context : Context,private val blist : List<ProgressFragDataClass>) : RecyclerView.Adapter<progressDownloadAdapter.ViewHolderProgress>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProgress {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.downloading_layout,parent,false)
        return progressDownloadAdapter.ViewHolderProgress(view)
    }

    override fun getItemCount(): Int {
       return blist.size
    }

    override fun onBindViewHolder(holder: ViewHolderProgress, position: Int) {
        TODO("Not yet implemented")
    }



    class ViewHolderProgress(itemView: View) : RecyclerView.ViewHolder(itemView) {



    }

}