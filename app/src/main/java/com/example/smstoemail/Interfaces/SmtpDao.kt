package com.example.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Query
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Entity.SmtpData

private const val tableName = "SmtpData"
lateinit var smtpDao: SmtpDao


@Dao
interface SmtpDao : BaseDao<SmtpData> {


    @Query("SELECT * FROM " + tableName)
    fun getAllItems(): List<SmtpData>


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(RecyclerMessage: RecyclerMessage)

//    @Delete
//    fun delete(RecyclerMessage: RecyclerMessage)



}