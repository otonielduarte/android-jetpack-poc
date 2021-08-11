package br.com.alura.technews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.alura.technews.database.dao.NewsDAO
import br.com.alura.technews.model.News

private const val DB_NAME = "news.db"

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val newsDAO: NewsDAO

    companion object {
        private lateinit var dbInstance: AppDatabase
        fun getInstance(context: Context): AppDatabase {

            if (::dbInstance.isInitialized) return dbInstance

            dbInstance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME
            ).build()

            return dbInstance
        }

    }

}