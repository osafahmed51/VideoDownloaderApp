package com.example.myapplication.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.util.Locale

class Utils() {


    fun getRootDirPath(context : Context) : String
    {
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState())
        {
            val file= ContextCompat.getExternalFilesDirs(context.applicationContext,null)[0]
            return file.absolutePath
        }
        else
        {
            return context.applicationContext.filesDir.absolutePath
        }

    }

    fun getCacheRootDirPath(context : Context) : String
    {
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState())
        {
            val file= ContextCompat.getExternalFilesDirs(context.applicationContext,null)[0]
            return file.absolutePath
        }
        else
        {
            return context.applicationContext.filesDir.absolutePath
        }

    }

    fun getBytesToMBString(bytes : Long) : String
    {
        return String.format(Locale.ENGLISH,  "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun getProgressDisplayLine(currentbytes : Long, totalbytes:Long) : String
    {
        return getBytesToMBString(currentbytes) + "/" + getBytesToMBString(totalbytes);
    }
}