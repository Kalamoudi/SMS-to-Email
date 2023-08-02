package com.example.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.TableNames
import com.example.smstoemail.Utils

private const val tableName = TableNames.RECYCLER_MESSAGE_TABLE
@Dao
interface ItemDao {
    @Query("SELECT * FROM " + tableName)
    fun getAllItems(): List<RecyclerMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(RecyclerMessage: RecyclerMessage)

    @Delete
    fun delete(RecyclerMessage: RecyclerMessage)
}