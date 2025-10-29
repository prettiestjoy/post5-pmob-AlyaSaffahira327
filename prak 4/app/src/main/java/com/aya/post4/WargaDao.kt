package com.aya.post4

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(warga: Warga)

    @Query("SELECT * FROM warga ORDER BY id ASC")
    fun getAllWarga(): LiveData<List<Warga>>

    @Query("DELETE FROM warga")
    fun deleteAll()

}