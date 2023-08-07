package com.example.smstoemail.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smstoemail.Entity.RecyclerMessage
import com.example.smstoemail.Interfaces.RecyclerMessageDao
import com.example.smstoemail.TypeConverters.CalendarTypeConverter

@Database(entities = [RecyclerMessage::class], version = 6)
@TypeConverters(CalendarTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recyclerMessageDao(): RecyclerMessageDao

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