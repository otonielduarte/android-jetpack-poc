package br.com.alura.technews.retrofit.webclient

import br.com.alura.technews.model.News
import br.com.alura.technews.retrofit.AppRetrofit
import br.com.alura.technews.retrofit.service.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val BAD_REQUEST = "Requisição não sucedida"

class NewsWebClient(
    private val service: NewsService = AppRetrofit().newsService
) {

    private fun <T> execute(
        call: Call<T>,
        onSuccess: (news: T?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(BAD_REQUEST)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t.message)
            }
        })
    }

    fun getAll(
        onSuccess: (news: List<News>?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(
            service.getAll(),
            onSuccess,
            onFailure
        )
    }

    fun save(
        news: News,
        onSuccess: (news: News?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(service.save(news), onSuccess, onFailure)
    }

    fun edita(
        id: Long,
        news: News,
        onSuccess: (savedNews: News?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(service.edita(id, news), onSuccess, onFailure)
    }

    fun remove(
        id: Long,
        onSuccess: (news: Void?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(service.remove(id), onSuccess, onFailure)
    }

}