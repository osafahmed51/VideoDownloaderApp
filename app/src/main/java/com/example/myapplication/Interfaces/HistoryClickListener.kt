package com.example.myapplication.Interfaces

import com.example.myapplication.Model.LinkEntity

interface HistoryClickListener {

    fun onDelete(linksentity : LinkEntity)

    fun onCopyLink(linksentity : LinkEntity)

    fun onShare(linksentity : LinkEntity)
}