package br.com.alura.technews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.alura.technews.asynctask.BaseAsyncTask
import br.com.alura.technews.database.dao.NewsDAO
import br.com.alura.technews.model.News
import br.com.alura.technews.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webclient: NewsWebClient = NewsWebClient()
) {


    private val mediator = MediatorLiveData<Resource<List<News>?>>()

    fun getAll(): LiveData<Resource<List<News>?>> {
        val lvListNews = MutableLiveData<Resource<List<News>?>>()

        mediator.addSource(getLocal()) {
            mediator.value = Resource(value = it)
        }

        mediator.addSource(lvListNews) { failureResource ->
            val currentValue = mediator.value
            val newResource: Resource<List<News>?> = if (currentValue != null) {
                Resource(value = currentValue.value, error = failureResource.error)
            } else {
                failureResource
            }
            mediator.value = newResource
        }

        return mediator
    }

    fun save(
        news: News,
    ): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        saveOnApi(news, onSuccess = {
            liveData.value = Resource(null)
        }, onFailure = {
            liveData.value = Resource(value = null, error = it)
        })
        return liveData
    }

    fun remove(
        news: News,
    ): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        removeOnApi(
            news,
            onSuccess = { liveData.value = Resource(null) },
            onFailure = { error -> liveData.value = Resource(null, error) }
        )
        return liveData;
    }

    fun edit(
        news: News,
    ): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        editOnApi(news, onSuccess = {
            liveData.value = Resource((null))
        }, onFailure = {
            liveData.value = Resource(value = null, error = it)
        })
        return liveData
    }

    fun getById(
        newsId: Long,
    ): LiveData<News?> {
        return dao.getById(newsId)
    }

    private fun getOnApi(
        onFailure: (error: String?) -> Unit
    ) {
        webclient.getAll(
            onSuccess = { news ->
                news?.let {
                    saveLocal(news)
                }
            }, onFailure = onFailure
        )
    }

    private fun getLocal() : LiveData<List<News>> {
        return dao.getAll()
    }


    private fun saveOnApi(
        news: News,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webclient.save(
            news,
            onSuccess = {
                it?.let { newsSaved ->
                    saveLocal(newsSaved, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun saveLocal(
        news: List<News>,
    ) {
        BaseAsyncTask(
            onExecute = {
                dao.saveList(news)

            }, onFinish = {}
        ).execute()
    }

    private fun saveLocal(
        news: News,
        onSuccess: () -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
        }, onFinish = {
            onSuccess()
        }).execute()

    }

    private fun removeOnApi(
        news: News,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webclient.remove(
            news.id,
            onSuccess = {
                removeLocal(news, onSuccess)
            },
            onFailure = onFailure
        )
    }


    private fun removeLocal(
        news: News,
        onSuccess: () -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.remove(news)
        }, onFinish = {
            onSuccess()
        }).execute()
    }

    private fun editOnApi(
        news: News,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webclient.edita(
            news.id, news,
            onSuccess = { editedNews ->
                editedNews?.let {
                    saveLocal(editedNews, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

}
