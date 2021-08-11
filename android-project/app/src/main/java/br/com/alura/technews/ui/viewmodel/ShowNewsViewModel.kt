package br.com.alura.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.repository.Resource

class ShowNewsViewModel(
    id: Long,
    private val repository: NewsRepository
) : ViewModel() {

    val foundNews = repository.getById(id)

    fun remove(): LiveData<Resource<Void?>> {
        return foundNews.value?.run {
            repository.remove(this)
        } ?: MutableLiveData<Resource<Void?>>().also {
            it.value = Resource(null, "Bad request")
        }
    }

}