package com.example.myapplication.Model

data class VideoInfo(
    val filePath: String,
    val sizeInMB: Any,
    val durationInSeconds: String,
    val thumbnail: String,
    val filename: String,
    var isSelected: Boolean = false,
    var deleteIconSelected : Boolean = false
):java.io.Serializable
