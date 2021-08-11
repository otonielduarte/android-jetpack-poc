package br.com.alura.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technews.model.News
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.repository.Resource

class ListNewsViewModel(private val repository: NewsRepository) : ViewModel() {

    fun getAll(): LiveData<Resource<List<News>?>> {
        return repository.getAll()
    }
}