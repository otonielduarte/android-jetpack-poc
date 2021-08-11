package br.com.alura.technews.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.alura.technews.model.News

@Dao
interface NewsDAO {

    @Query("SELECT * FROM News ORDER BY id DESC")
    fun getAll(): LiveData<List<News>>

    @Insert(onConflict = REPLACE)
    fun save(news: News)

    @Delete
    fun remove(news: News)

    @Query("SELECT * FROM News WHERE id = :id")
    fun getById(id: Long): LiveData<News?>

    @Insert(onConflict = REPLACE)
    fun saveList(news: List<News>)

}
