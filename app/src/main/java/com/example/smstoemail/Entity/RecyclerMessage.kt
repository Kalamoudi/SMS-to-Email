package com.example.smstoemail.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smstoemail.Sms.SmsData
import java.time.DayOfWeek
import java.util.Calendar


//@Entity(tableName = "RecyclerMessage")
//data class RecyclerMessage(
//    @PrimaryKey(autoGenerate = true)
//    var id: Long = 0,
////    var sender: String = "",
////    var recipient: String = "",
////    var messageBody: String = "",
//    var encryptedSenderNumber: ByteArray,
//    var encryptedRecipientNumber: ByteArray,
//    var encryptedMessageBody: ByteArray,
//    var calendar: Calendar = Calendar.getInstance()
//)

@Entity(tableName = "RecyclerMessage")
data class RecyclerMessage(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var encryptedSenderNumber: ByteArray,
    var encryptedRecipientNumber: ByteArray,
    var encryptedMessageBody: ByteArray,
    var calendar: Calendar = Calendar.getInstance()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecyclerMessage) return false

        return id == other.id &&
                encryptedSenderNumber.contentEquals(other.encryptedSenderNumber) &&
                encryptedRecipientNumber.contentEquals(other.encryptedRecipientNumber) &&
                encryptedMessageBody.contentEquals(other.encryptedMessageBody) &&
                calendar == other.calendar
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + encryptedSenderNumber.contentHashCode()
        result = 31 * result + encryptedRecipientNumber.contentHashCode()
        result = 31 * result + encryptedMessageBody.contentHashCode()
        result = 31 * result + calendar.hashCode()
        return result
    }
}