package br.com.alura.technews.retrofit.webclient

import br.com.alura.technews.model.News
import br.com.alura.technews.retrofit.AppRetrofit
import br.com.alura.technews.retrofit.service.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsWebClient(
    private val service: NewsService = AppRetrofit().newsService
) {

    fun getAll(): List<News>? {
        return service.getAll().execute().body()
    }

    fun save(news: News): News? {
        return service.save(news).execute().body()
    }

    fun edit( id: Long, news: News): News? {
        return service.edita(id, news).execute().body()
    }

    fun remove(id: Long) : Boolean? {
        return service.remove(id).execute().isSuccessful
    }

}