package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Fragments.progressFragment
import com.example.myapplication.Model.Entry
import com.example.myapplication.R
import com.example.myapplication.utils.Utils

class downloadprogressAdapter (private val context: Context,private val dList:List<Entry>) : RecyclerView.Adapter<downloadprogressAdapter.ViewHolder>() {

    var path : String? = null
    var BaseURL : String? = null
    var downloadID : Int? = null
    var filename : String? = null
    lateinit var entrydata: Entry
    var utilss= Utils()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.downloading_layout,parent,false)


        utilss= Utils()
        path=utilss.getRootDirPath(context)



        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val progressFragment=progressFragment()


    }




    class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

        val videoTitle : TextView =itemview.findViewById(R.id.videotitle_progress)
        val progressbar : ProgressBar=itemview.findViewById(R.id.progressBar_download_progress)
        val close:TextView=itemview.findViewById(R.id.closebtn_progress)
        val mbstxtv:TextView=itemview.findViewById(R.id.mbs_textv_progress)
        val speedtxtv:TextView=itemview.findViewById(R.id.speedtextv_progress)
        val globeimgv:ImageView=itemview.findViewById(R.id.globeimgv_progress)
        val pauseimgv:ImageView=itemview.findViewById(R.id.pauseimgv_progress)


    }
}