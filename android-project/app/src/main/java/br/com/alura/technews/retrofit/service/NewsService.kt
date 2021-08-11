package br.com.alura.technews.retrofit.service

import br.com.alura.technews.model.News
import retrofit2.Call
import retrofit2.http.*

interface NewsService {

    @GET("noticias")
    fun getAll(): Call<List<News>>

    @POST("noticias")
    fun save(@Body news: News): Call<News>

    @PUT("noticias/{id}")
    fun edita(@Path("id") id: Long, @Body news: News) : Call<News>

    @DELETE("noticias/{id}")
    fun remove(@Path("id") id: Long): Call<Void>

}
