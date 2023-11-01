package com.example.myapplication.Interfaces

import android.net.Uri

interface StatusClickListener {
    fun onImageViewClick(uri: Uri?, title: String)
}