package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.News
import br.com.alura.technews.repository.NewsRepository
import br.com.alura.technews.ui.activity.extensions.showError
import br.com.alura.technews.ui.recyclerview.adapter.ListNewsAdapter
import br.com.alura.technews.ui.viewmodel.ListNewsViewModel
import br.com.alura.technews.ui.viewmodel.factory.ListNewsViewModelFactory
import kotlinx.android.synthetic.main.activity_lista_noticias.*

private const val APPBAR_TITLE = "News"
private const val FAILURE_MESSAGE = "Something is wrong when getting news"

class ListNewsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = ListNewsViewModelFactory(repository)
        val provider = ViewModelProvider(this, factory)
        provider.get(ListNewsViewModel::class.java)
    }

    private val adapter by lazy {
        ListNewsAdapter(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)
        title = APPBAR_TITLE
        createRecyclerView()
        handleFabButton()
        getNews()
    }

    private fun handleFabButton() {
        activity_lista_noticias_fab_salva_noticia.setOnClickListener {
            showFormNews()
        }
    }

    private fun createRecyclerView() {
        val divisor = DividerItemDecoration(this, VERTICAL)
        activity_lista_noticias_recyclerview.addItemDecoration(divisor)
        activity_lista_noticias_recyclerview.adapter = adapter
        createAdapter()
    }

    private fun createAdapter() {
        adapter.onClick = this::showNews
    }

    private fun getNews() {
        viewModel.getAll().observe(
            this,
            Observer { resource ->
                resource.value?.let { adapter.update(it) }
                resource.error?.let { showError(FAILURE_MESSAGE) }
            },
        )
    }

    private fun showFormNews() {
        val intent = Intent(this, FormNewsActivity::class.java)
        startActivity(intent)
    }

    private fun showNews(it: News) {
        val intent = Intent(this, ShowNewsActivity::class.java)
        intent.putExtra(NEWS_KEY_ID, it.id)
        startActivity(intent)
    }

}
