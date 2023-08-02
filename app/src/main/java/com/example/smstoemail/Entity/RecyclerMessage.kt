package com.example.smstoemail.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData

@Entity(tableName = "RecyclerMessages")
data class RecyclerMessages(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var smsData: SmsData

)