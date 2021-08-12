package br.com.alura.technews.ui.fragment

import android.app.ListActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.R
import br.com.alura.technews.ui.activity.ListNewsActivity
import br.com.alura.technews.ui.fragment.extensions.showError
import br.com.alura.technews.ui.recyclerview.adapter.ListNewsAdapter
import br.com.alura.technews.ui.viewmodel.ListNewsViewModel
import kotlinx.android.synthetic.main.list_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val FAILURE_MESSAGE = "Something is wrong when getting news"

class ListNewsFragment : Fragment() {

    //private val database by inject<AppDatabase>()
    private val viewModel by viewModel<ListNewsViewModel>()
    private lateinit var listNewsActivity: ListNewsActivity

    private val adapter by lazy {
        context?.let {
            ListNewsAdapter(context = it)
        } ?: throw IllegalArgumentException("Invalid context")
    }

    // Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getNews()
        listNewsActivity = activity as ListNewsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView()
        handleFabButton()
    }

    private fun handleFabButton() {
        lista_noticias_fab_salva_noticia.setOnClickListener {
            listNewsActivity.showFormNews()
        }
    }

    private fun createRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        lista_noticias_recyclerview.addItemDecoration(divisor)
        lista_noticias_recyclerview.adapter = adapter
        createAdapter()
    }

    private fun createAdapter() {
        adapter.onClick = listNewsActivity::showNews
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
}