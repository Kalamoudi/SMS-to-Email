package com.kuaapps.smstoemail.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuaapps.smstoemail.Entity.RecyclerMessage
import com.kuaapps.smstoemail.Entity.SmsFilterRecyclerMessage
import com.kuaapps.smstoemail.Entity.SmtpData
import com.kuaapps.smstoemail.Interfaces.RecyclerMessageDao
import com.kuaapps.smstoemail.Interfaces.SmsFilterRecyclerMessageDao
import com.kuaapps.smstoemail.Interfaces.SmtpDao
import com.kuaapps.smstoemail.TypeConverters.CalendarTypeConverter

@Database(entities = [RecyclerMessage::class, SmtpData::class, SmsFilterRecyclerMessage::class], version = 10)
@TypeConverters(CalendarTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recyclerMessageDao(): RecyclerMessageDao
    abstract fun smtpDao(): SmtpDao
    abstract fun smsFilterRecyclerMessageDao(): SmsFilterRecyclerMessageDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(context) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).fallbackToDestructiveMigration() // Add the missing migration strategy here
                    .build().also { instance = it }
            }
        }


    }
}