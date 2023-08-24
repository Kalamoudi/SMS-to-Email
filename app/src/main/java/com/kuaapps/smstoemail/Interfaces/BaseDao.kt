package com.kuaapps.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T)

    @Delete
    fun delete(item: T)

}