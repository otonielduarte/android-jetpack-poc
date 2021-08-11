package br.com.alura.technews.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.ui.viewmodel.FormNewsViewModel
import br.com.alura.technews.ui.viewmodel.ListNewsViewModel

class FormNewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormNewsViewModel(repository) as T
    }
}