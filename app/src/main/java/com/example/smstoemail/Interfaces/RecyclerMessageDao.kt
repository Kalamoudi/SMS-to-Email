package com.example.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.TableNames
import com.example.smstoemail.Utils

//private const val tableName = TableNames.RECYCLER_MESSAGE_TABLE

private const val tableName = "RecyclerMessage"
lateinit var recyclerMessageDao: RecyclerMessageDao

@Dao
interface RecyclerMessageDao : BaseDao<RecyclerMessage> {


    @Query("SELECT * FROM " + tableName)
    fun getAllItems(): List<RecyclerMessage>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(RecyclerMessage: RecyclerMessage)

//    @Delete
//    fun delete(RecyclerMessage: RecyclerMessage)



}