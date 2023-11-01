package com.example.myapplication.Interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Model.BookmarkEntity

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insert(bookmarkEntity: BookmarkEntity)

    @Query("SELECT * FROM bookmarks")
    suspend fun getAllUsers(): List<BookmarkEntity>

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAll()

    @Query("DELETE FROM bookmarks WHERE id = :bookmarklinkId")
    suspend fun deleteUserById(bookmarklinkId: Long)



}