package br.com.alura.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technews.model.News
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.repository.Resource

class FormNewsViewModel(private val repository: NewsRepository) : ViewModel() {

    fun getById(newsId: Long) = repository.getById(newsId)

    fun save(news: News): LiveData<Resource<Void?>> {
        if (news.id > 0) {
            return repository.edit(news)
        }
        return repository.save(news = news)
    }
}