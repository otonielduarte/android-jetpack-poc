package br.com.alura.technews.di.modules

import androidx.room.Room
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.database.dao.NewsDAO
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.retrofit.webclient.NewsWebClient
import br.com.alura.technews.ui.viewmodel.FormNewsViewModel
import br.com.alura.technews.ui.viewmodel.ListNewsViewModel
import br.com.alura.technews.ui.viewmodel.ShowNewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DB_NAME = "news.db"

val appModules = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    single<NewsDAO> {
        get<AppDatabase>().newsDAO
    }

    single<NewsWebClient> {
        NewsWebClient()
    }

    single<NewsRepository> {
        NewsRepository(get(), get())
    }

    viewModel<ListNewsViewModel> {
        ListNewsViewModel(get())
    }

    viewModel<ShowNewsViewModel> { (id: Long) ->
        ShowNewsViewModel(id, get())
    }

    viewModel<FormNewsViewModel> {
        FormNewsViewModel(get())
    }
}