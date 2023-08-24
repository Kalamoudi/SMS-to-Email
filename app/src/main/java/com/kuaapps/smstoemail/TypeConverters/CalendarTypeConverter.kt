package com.kuaapps.smstoemail.TypeConverters

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarTypeConverter {
    @TypeConverter
    fun fromCalendar(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }

    @TypeConverter
    fun toCalendar(timestamp: Long?): Calendar? {
        return timestamp?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar
        }
    }
}