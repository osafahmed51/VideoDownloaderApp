package com.example.myapplication.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.Interfaces.BookmarkDao
import com.example.myapplication.Interfaces.LinkDao
import com.example.myapplication.Model.BookmarkEntity
import com.example.myapplication.Model.LinkEntity

@Database(entities = [LinkEntity::class, BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){
    abstract fun linkDao(): LinkDao
    abstract fun bookmarkDao(): BookmarkDao

}