package com.example.myapplication.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Sliderdata
import com.example.myapplication.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdpater(val imagelist:ArrayList<Sliderdata>) : SliderViewAdapter<SliderAdpater.SliderAdapterVH>() {


    override fun getCount(): Int {
        return imagelist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.customslideritemlayout,parent,false)
        return SliderAdapterVH(view)

    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        val imageData=imagelist[position]
        if (viewHolder != null) {
            Glide.with(viewHolder.itemView)
                .load(imageData.resourceId)
                .into(viewHolder.imageView)
        }
    }


    class SliderAdapterVH(itemView:View): SliderViewAdapter.ViewHolder(itemView) {

        val imageView:ImageView=itemView.findViewById(R.id.iv_auto_image_slider)
    }


}