package com.example.myapplication.Interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Model.LinkEntity

@Dao
interface LinkDao {
    @Insert
    suspend fun insert(linkEntity: LinkEntity)

    @Query("SELECT * FROM links")
    suspend fun getAllUsers(): List<LinkEntity>

    @Query("DELETE FROM links")
    suspend fun deleteAll()

    @Query("DELETE FROM links WHERE id = :linkId")
    suspend fun deleteUserById(linkId: Long)

}