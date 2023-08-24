package com.kuaapps.smstoemail.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SmsFilterRecyclerMessage")
data class SmsFilterRecyclerMessage(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var filter: String

)