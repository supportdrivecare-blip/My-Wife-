package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        WifeProfile::class,
        GiftWish::class,
        DailyCareItem::class,
        LoveNote::class,
        DateIdea::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WifeDatabase : RoomDatabase() {
    abstract fun wifeDao(): WifeDao

    companion object {
        @Volatile
        private var INSTANCE: WifeDatabase? = null

        fun getDatabase(context: Context): WifeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WifeDatabase::class.java,
                    "meri_wife_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
