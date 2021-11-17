package com.example.flo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Album::class], version = 3)
abstract class SongDatabase: RoomDatabase() {
    abstract fun AlbumDao(): AlbumDao
    abstract fun SongDao(): SongDao

    companion object {
        private var instance:SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "user-database" // 다른 데이터베이스랑 이름겹치면 꼬이기 때문
                    ).allowMainThreadQueries().build() // 워크스레드가 아닌 메인스레드에 맡겨줌
                }
            }
            return instance
        }
    }
}