package com.example.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Entity.SmsFilterRecyclerMessage


private const val tableName = "SmsFilterRecyclerMessage"
private const val columnName = "filter"
lateinit var smsFilterRecyclerMessageDao: SmsFilterRecyclerMessageDao

@Dao
interface SmsFilterRecyclerMessageDao : BaseDao<SmsFilterRecyclerMessage> {

    @Query("SELECT * FROM " + tableName)
    fun getAllItems(): List<SmsFilterRecyclerMessage>

    @Query("SELECT * FROM " + tableName + " WHERE " + columnName + " = :filter LIMIT 1")
    fun getSmsFilter(filter: String): SmsFilterRecyclerMessage

    @Query("DELETE FROM " + tableName)
    fun deleteAllSmsFilters()



}