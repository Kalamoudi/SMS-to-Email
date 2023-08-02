package com.example.smstoemail.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData

@Entity(tableName = "RecyclerMessage")
data class RecyclerMessage(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var sender: String = "",
    var recipient: String = "",
    var messageBody: String = "",

)