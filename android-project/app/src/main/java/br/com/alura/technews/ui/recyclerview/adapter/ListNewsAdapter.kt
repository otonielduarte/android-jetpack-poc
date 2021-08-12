package br.com.alura.technews.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.technews.R
import br.com.alura.technews.model.News
import kotlinx.android.synthetic.main.item_new.view.*

class ListNewsAdapter(
    private val context: Context,
    private val news: MutableList<News> = mutableListOf(),
    var onClick: (news: News) -> Unit = {}
) : RecyclerView.Adapter<ListNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val createdView = LayoutInflater.from(context)
            .inflate(
                R.layout.item_new,
                parent, false
            )
        return ViewHolder(createdView)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notice = news[position]
        holder.bind(notice)
    }

    fun update(news: List<News>) {
        notifyItemRangeRemoved(0, this.news.size)
        this.news.clear()
        this.news.addAll(news)
        notifyItemRangeInserted(0, this.news.size)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var news: News

        init {
            itemView.setOnClickListener {
                if (::news.isInitialized) {
                    onClick(news)
                }
            }
        }

        fun bind(news: News) {
            this.news = news
            itemView.item_noticia_titulo.text = news.title
            itemView.item_noticia_texto.text = news.text
        }

    }

}
