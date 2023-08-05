package com.example.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smstoemail.Entity.RecyclerMessage

@Dao
interface BaseDao<T> {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T)

    @Delete
    fun delete(item: T)

}