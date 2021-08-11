package br.com.alura.technews.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.News
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.ui.activity.extensions.showError
import br.com.alura.technews.ui.viewmodel.FormNewsViewModel
import br.com.alura.technews.ui.viewmodel.factory.FormNewsViewModelFactory
import kotlinx.android.synthetic.main.activity_formulario_noticia.*

private const val TITLE_ON_EDIT = "Edit News"
private const val TITLE = "Create News"
private const val ERROR_MESSAGE_ON_SAVE = "Failure to save news"

class FormNewsActivity : AppCompatActivity() {

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_KEY_ID, 0)
    }

    private val viewModel: FormNewsViewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = FormNewsViewModelFactory(repository)
        val provider = ViewModelProvider(this, factory)
        provider.get(FormNewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_noticia)
        handleTitle()
        bindForm()
    }

    private fun handleTitle() {
        title = if (newsId > 0) {
            TITLE_ON_EDIT
        } else {
            TITLE
        }
    }

    private fun bindForm() {
        viewModel.getById(newsId).observe(this, Observer { found ->
            if(found != null) {
                activity_formulario_noticia_titulo.setText(found.title)
                activity_formulario_noticia_texto.setText(found.text)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.formulario_noticia_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.formulario_noticia_salva -> {
                val title = activity_formulario_noticia_titulo.text.toString()
                val text = activity_formulario_noticia_texto.text.toString()
                save(News(newsId, title, text))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save(news: News) {
        viewModel.save(news).observe(this, {
            if (it.error == null) {
                finish()
            } else {
                showError(ERROR_MESSAGE_ON_SAVE)
            }
        })
    }
}
