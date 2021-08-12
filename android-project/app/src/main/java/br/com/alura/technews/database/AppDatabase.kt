package br.com.alura.technews.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.alura.technews.database.dao.NewsDAO
import br.com.alura.technews.model.News



@Database(entities = [News::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val newsDAO: NewsDAO
}