package com.example.myapplication.Adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.BottomMainList
import com.example.myapplication.R
import kotlin.coroutines.coroutineContext

class bottomSheetAdapter(private var context: Context,private val bList:List<BottomMainList>,private val onClickListener : OnClickListener) :
    RecyclerView.Adapter<bottomSheetAdapter.ViewHolder>() {

    var selectedposition=0




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.bottomsheetrecyclerview_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return bList.size

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel=bList[position]


        holder.textv1.text=ItemsViewModel.FBQualityLabel
        holder.textv2.text = String.format("%.2f",ItemsViewModel.size ) + " MB"



        if(selectedposition == position)
        {
            holder.textv1.background=ContextCompat.getDrawable(context,R.drawable.selectedbottombuttonbackground)
        }
        else
        {
            holder.textv1.background=ContextCompat.getDrawable(context,R.drawable.bottombuttons_background)
        }



        holder.itemView.setOnClickListener {


            Log.d("ab_click", "onBindViewHolder: on click in adapter")

                val previousSelectedPosition = selectedposition
                selectedposition = position
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedposition)


            onClickListener!!.onClick(selectedposition,ItemsViewModel)

        }

    }



    interface OnClickListener {
        fun onClick(position: Int, model: BottomMainList)
    }


     class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        val textv1:TextView=itemview.findViewById(R.id.bottomsheet_textvone)
        val textv2:TextView=itemview.findViewById(R.id.bottomsheettextvtwo)


    }

}