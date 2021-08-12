package br.com.alura.technews.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.alura.technews.database.dao.NewsDAO
import br.com.alura.technews.model.News
import br.com.alura.technews.retrofit.webclient.NewsWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.IOException

class NewsRepository(
    private val dao: NewsDAO,
    private val webclient: NewsWebClient
) {
    private val mediator = MediatorLiveData<Resource<List<News>?>>()

    fun getAll(): LiveData<Resource<List<News>?>> {
        mediator.addSource(getLocal()) {
            mediator.value = Resource(value = it)
        }
        mediator.addSource(getOnApi()) { failureResource ->
            val currentValue = mediator.value
            val newResource: Resource<List<News>?> = getResourceApi(currentValue, failureResource)
            mediator.value = newResource
        }

        return mediator
    }

    private fun getResourceApi(
        currentValue: Resource<List<News>?>?,
        failureResource: Resource<List<News>?>
    ): Resource<List<News>?> {
        return if (currentValue != null) {
            Resource(value = currentValue.value, error = failureResource.error)
        } else {
            failureResource
        }
    }

    fun save(
        news: News,
    ): LiveData<Resource<Void?>> = saveOnApi(news)

    fun remove(
        news: News,
    ): LiveData<Resource<Void?>> = removeOnApi(news)

    fun edit(
        news: News,
    ): LiveData<Resource<Void?>> = editOnApi(news)

    fun getById(
        newsId: Long,
    ): LiveData<News?> {
        return dao.getById(newsId)
    }

    private fun getLocal(): LiveData<List<News>> {
        return dao.getAll()
    }

    private fun getOnApi(
    ): LiveData<Resource<List<News>?>> {
        return MutableLiveData<Resource<List<News>?>>().also { liveData ->
            val scope = CoroutineScope(IO)
            scope.launch {
                val resource: Resource<List<News>?> = getResourceListNews()
                liveData.postValue(resource)
            }
        }
    }

    private fun saveOnApi(news: News): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also {
            val coroutineScope = CoroutineScope(IO)
            coroutineScope.launch {
                val resource: Resource<Void?> = getResourceSaveNew(news)
                it.postValue(resource)
            }
        }
    }

    private fun editOnApi(
        news: News,
    ): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also {
            val coroutineScope = CoroutineScope(IO)
            coroutineScope.launch {
                val resource: Resource<Void?> = getResourceEditNew(news)
                it.postValue(resource)
            }
        }
    }

    private fun removeOnApi(
        news: News,
    ): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also {
            val coroutineScope = CoroutineScope(IO)
            coroutineScope.launch {
                val resource: Resource<Void?> = getResourceRemoveNew(news)
                it.postValue(resource)
            }
        }
    }

    private fun getResourceListNews(): Resource<List<News>?> {
        return try {
            webclient.getAll()?.let { listNews ->
                dao.saveList(listNews)
            }
            Resource(value = null)
        } catch (e: IOException) {
            Resource(value = null, error = e.message)
        }
    }

    private fun getResourceSaveNew(news: News): Resource<Void?> {
        return try {
            webclient.save(news)?.let { savedNew ->
                dao.save(savedNew)
            }
            Resource(value = null)
        } catch (e: IOException) {
            Resource(value = null, error = e.message)
        }
    }

    private fun getResourceEditNew(news: News): Resource<Void?> {
        return try {
            webclient.edit(news.id, news)?.let { editedNew ->
                dao.save(editedNew)
            }
            Resource(value = null)
        } catch (e: IOException) {
            Resource(value = null)
        }
    }

    private fun getResourceRemoveNew(news: News): Resource<Void?> {
        return try {
            webclient.remove(news.id)?.let {
                Log.i("DEBUG", "removed dao")
                dao.remove(news)
            }
            Log.i("DEBUG", "removed")
            Resource(value = null)
        } catch (e: IOException) {
            Log.i("DEBUG", "not removed")
            Resource(value = null, error = e.message)
        }
    }
}
