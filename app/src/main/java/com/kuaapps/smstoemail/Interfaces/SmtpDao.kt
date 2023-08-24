package com.kuaapps.smstoemail.Interfaces

import androidx.room.Dao
import androidx.room.Query
import com.kuaapps.smstoemail.Entity.SmtpData

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