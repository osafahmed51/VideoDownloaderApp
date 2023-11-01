package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Interfaces.DownloadFragItemClickListener
import com.example.myapplication.Model.VideoInfo
import com.example.myapplication.R

class FinishAdapter(private val dlist : List<VideoInfo>, private var context: Context, private val downfragclicklistener : DownloadFragItemClickListener) : RecyclerView.Adapter<FinishAdapter.ViewHolderDownload>() {


    var clicked : Boolean=false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDownload {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.downloadfragmentlayout,parent,false)
        return FinishAdapter.ViewHolderDownload(view)
    }

    fun deleteIconClicked()
    {
        val deleteIconSelect=dlist.all { it.deleteIconSelected }
        for(deleteiconselect in dlist)
        {
            clicked=!deleteIconSelect
        }
        notifyDataSetChanged()
    }

    fun checkboxSelectAll()
    {
        val selectAll = dlist.all { it.isSelected }

        for(videoisselected in dlist)
        {
            videoisselected.isSelected= !selectAll
        }
        notifyDataSetChanged()

    }
    override fun onBindViewHolder(holder: ViewHolderDownload, position: Int) {
        val itemsViewModel=dlist[position]

        Glide.with(context).load(itemsViewModel.filePath).into(holder.videoimgv)
        holder.textv1_videoname.text=itemsViewModel.filename
        holder.textv2_videosize.text= "${String.format("%.2f",itemsViewModel.sizeInMB )} MB"


        if(clicked)
        {
            holder.finishdotsimgv.visibility=View.INVISIBLE
            holder.checkboxfinish.visibility=View.VISIBLE

        }
        else
        {
        holder.finishdotsimgv.visibility=View.VISIBLE
        holder.checkboxfinish.visibility=View.INVISIBLE
        }

        holder.checkboxfinish.isChecked=itemsViewModel.isSelected

        holder.checkboxfinish.setOnClickListener {
            itemsViewModel.isSelected=holder.checkboxfinish.isChecked
            notifyItemChanged(position)
        }

        holder.videoimgv.setOnClickListener {
            downfragclicklistener.onPlayClick(itemsViewModel)
        }

        holder.finishdotsimgv.setOnClickListener {
            val popupMenu = PopupMenu(context,holder.finishdotsimgv)
            popupMenu.menuInflater.inflate(R.menu.finish_optionmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.play -> {
                        downfragclicklistener.onPlayClick(itemsViewModel)
                        true
                    }
                    R.id.delete -> {
                        downfragclicklistener.onDeleteClick(itemsViewModel)
                        true
                    }
                    R.id.share -> {
                        downfragclicklistener.onShareClick(itemsViewModel)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }






    }

    override fun getItemCount(): Int {
        return dlist.size
    }



    class ViewHolderDownload(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textv1_videoname: TextView =itemView.findViewById(R.id.finish_videonametxtv)
        val textv2_videosize: TextView =itemView.findViewById(R.id.finish_txtvsize)
        val videoimgv : ImageView=itemView.findViewById(R.id.finish_imgv)
        val finishdotsimgv : ImageView=itemView.findViewById(R.id.finish_dotsthree)
        val checkboxfinish : CheckBox=itemView.findViewById(R.id.finish_checkbox)

    }

}