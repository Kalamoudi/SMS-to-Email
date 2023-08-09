package com.example.smstoemail.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData
import java.time.DayOfWeek
import java.util.Calendar

@Entity(tableName = "SmtpData")
data class SmtpData (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var host: String = "",
    var port: String = "",
    var username: String = "",
    var password: String = ""

)