package com.example.myapplication.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

class DirectoryUtils {

    private fun createAppFolder(context: Context): String {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            context.externalMediaDirs.forEach {
//                if (it.name.contains(context.packageName)) {
//                    return it.absolutePath + File.separator + AppConstants.APP_FOLDER_NAME
//                }
//            }
//        } else {
//            return Environment.getExternalStorageDirectory().absolutePath + File.separator + AppConstants.APP_FOLDER_NAME
//        }
//        return Environment.getExternalStorageDirectory().absolutePath + File.separator + AppConstants.APP_FOLDER_NAME
//    }
        return ""
    }
}