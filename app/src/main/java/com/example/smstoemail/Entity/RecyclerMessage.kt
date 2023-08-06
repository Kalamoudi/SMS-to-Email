package com.example.smstoemail.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData
import java.time.DayOfWeek

@Entity(tableName = "RecyclerMessage")
data class RecyclerMessage(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var sender: String = "",
    var recipient: String = "",
    var messageBody: String = "",
    var dayOfWeek: String = "",
    var day: String = "",
    var month: String = "",
    var year: String = "",
    var seconds: String = "",
    var minutes: String = "",
    var hour: String = "",
    var meridiem: String = ""
)