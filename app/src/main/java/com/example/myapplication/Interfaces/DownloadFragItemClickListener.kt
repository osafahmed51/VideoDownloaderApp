package com.example.myapplication.Interfaces

import com.example.myapplication.Model.VideoInfo

interface DownloadFragItemClickListener {

        fun onPlayClick(videoInfo: VideoInfo)
        fun onDeleteClick(videoInfo: VideoInfo)
        fun onShareClick(videoInfo: VideoInfo)

}