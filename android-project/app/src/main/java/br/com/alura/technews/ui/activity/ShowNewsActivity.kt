package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.News
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.ui.activity.extensions.showError
import br.com.alura.technews.ui.viewmodel.ShowNewsViewModel
import br.com.alura.technews.ui.viewmodel.factory.ShowNewsViewModelFactory
import kotlinx.android.synthetic.main.activity_visualiza_noticia.*

private const val NEWS_NOT_FOUND = "News not found"
private const val APPBAR_TITLE = "News"
private const val BAD_REQUEST_ON_REMOVE = "Failure to remove news"

class ShowNewsActivity : AppCompatActivity() {

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_KEY_ID, 0)
    }

    private val viewModel: ShowNewsViewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = ShowNewsViewModelFactory(newsId, repository)
        val provider = ViewModelProvider(this, factory)
        provider.get(ShowNewsViewModel::class.java)
    }

    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualiza_noticia)
        title = APPBAR_TITLE
        checkNewsId()
        getSelectedNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visualiza_noticia_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.visualiza_noticia_menu_edita -> showEditForm()
            R.id.visualiza_noticia_menu_remove -> remove()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSelectedNews() {
        viewModel.foundNews.observe(this, {
            it?.let {
                this.news = it
                bindFields(it)
            }
        })
    }

    private fun checkNewsId() {
        if (newsId == 0L) {
            showError(NEWS_NOT_FOUND)
            finish()
        }
    }

    private fun bindFields(news: News) {
        activity_visualiza_noticia_titulo.text = news.title
        activity_visualiza_noticia_texto.text = news.text
    }

    private fun remove() {
        if (::news.isInitialized) {
            viewModel.remove().observe(this, {
                it.error?.run {
                    showError(BAD_REQUEST_ON_REMOVE)
                } ?: finish()
            })
        }
    }

    private fun showEditForm() {
        val intent = Intent(this, FormNewsActivity::class.java)
        intent.putExtra(NEWS_KEY_ID, newsId)
        startActivity(intent)
    }

}
