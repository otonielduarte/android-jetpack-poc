package br.com.alura.technews.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.ui.viewmodel.ShowNewsViewModel

class ShowNewsViewModelFactory(
    private val id: Long,
    private val repository: NewsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShowNewsViewModel(id, repository) as T
    }
}