package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences


class SharePreferenceClass(var context: Context?) {
    private val VIDEO_PLAYER = "VIDEO_DOWNLOADER"
    private var editor: SharedPreferences.Editor? = null
    private var pref: SharedPreferences? = null
    private val SYNC_TO_GALLERY = "sync_togallery"
    private val search_engine = "Search_Engine"

    init {
        if (context != null) {
            pref = context!!.getSharedPreferences(VIDEO_PLAYER, Context.MODE_PRIVATE)
        }
        editor = pref?.edit()
    }


    fun setSyncGallerySettings(context: Context, z: Boolean) {
        val edit = context.getSharedPreferences(VIDEO_PLAYER, 0).edit()
        edit.putBoolean(SYNC_TO_GALLERY, z)
        edit.apply()
    }

    fun getSyncGallerySettings(context: Context): Boolean {
        return context.getSharedPreferences(VIDEO_PLAYER, 0).getBoolean(SYNC_TO_GALLERY, true)
    }

    fun saveSelectedOption(selectedOption: String?) {
        val sharedPreferences: SharedPreferences? =
            context?.getSharedPreferences("search_engine", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putString("selectedOption", selectedOption)
        }
        if (editor != null) {
            editor.apply()
        }
    }

    fun getSelectedOption(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("search_engine", 0)
        return sharedPreferences.getString("selectedOption", "Google")
    }


    fun getString(PREF_NAME: String?): String? {
        return pref!!.getString(PREF_NAME, "")
    }

    fun setString(PREF_NAME: String?, `val`: String?) {
        editor!!.putString(PREF_NAME, `val`)
        editor!!.commit()
    }
}