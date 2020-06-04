package com.flickPick.db

import android.content.Context
import androidx.room.*

@Database(entities = [UserProfile::class], version = 1, exportSchema = false)
@TypeConverters(SpecialFilmsConverter::class)
abstract class FilmDatabase: RoomDatabase() {
    abstract val filmDatabaseDao: FilmDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null
        fun getInstance(context: Context): FilmDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        FilmDatabase::class.java, "filmDatabase")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}