package com.example.myapplication.Interfaces

import com.example.myapplication.Model.BookmarkEntity

interface BookmarkClickListener {

    fun onDelete(linksentity : BookmarkEntity)

    fun onCopyLink(linksentity : BookmarkEntity)

    fun onShare(linksentity : BookmarkEntity)
}