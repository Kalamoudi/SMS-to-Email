package com.example.smstoemail.Sms

import com.example.smstoemail.Utils
import java.util.Calendar

class SmsStrings (sender: String, recipient: String, messageBody: String, calendar: Calendar) {

    var sender: String = sender
    var recipient: String = recipient
    var messageBody: String = messageBody

    var calendar: Calendar = calendar

    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Unknown"
    }
    val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
    val month = (calendar.get(Calendar.MONTH) + 1).toString()
    val year = calendar.get(Calendar.YEAR).toString()
    val seconds = calendar.get(Calendar.SECOND).toString()
    val minutes = calendar.get(Calendar.MINUTE).toString()
    val hour = calendar.get(Calendar.HOUR).toString()
    val meridiem = calendar.get(Calendar.AM_PM).toString()


    inner class binder{

        val header = this@SmsStrings.dayOfWeek + ", " +
                     Utils.getMonthAbbreviation(this@SmsStrings.month) + " " +
                     this@SmsStrings.day + ", " +
                     this@SmsStrings.year + " • " +
                     this@SmsStrings.hour + ":" +
                     this@SmsStrings.minutes + " " +
                     Utils.getMeridiem(this@SmsStrings.meridiem)

        val sender = "From:  " + this@SmsStrings.sender
        val recipient = "To:       +" + this@SmsStrings.recipient
        val messageBody = this@SmsStrings.messageBody
    }

}