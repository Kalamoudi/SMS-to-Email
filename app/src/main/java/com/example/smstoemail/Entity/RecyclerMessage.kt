package com.example.smstoemail.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData
import java.time.DayOfWeek
import java.util.Calendar


@Entity(tableName = "RecyclerMessage")
data class RecyclerMessage(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var sender: String = "",
    var recipient: String = "",
    var messageBody: String = "",
    var calendar: Calendar = Calendar.getInstance()
)