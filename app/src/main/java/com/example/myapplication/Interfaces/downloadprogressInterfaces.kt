package com.example.myapplication.Interfaces

import android.widget.ProgressBar
import android.widget.TextView

interface downloadprogressInterfaces {
    fun downloadFile(url : String, progressbar : ProgressBar, path:String, filename : String, mbstextv: TextView)
}