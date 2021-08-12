package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.technews.R
import br.com.alura.technews.model.News

private const val APPBAR_TITLE = "News"


class ListNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)
        title = APPBAR_TITLE
    }

    fun showFormNews() {
        val intent = Intent(this, FormNewsActivity::class.java)
        startActivity(intent)
    }

    fun showNews(it: News) {
        val intent = Intent(this, ShowNewsActivity::class.java)
        intent.putExtra(NEWS_KEY_ID, it.id)
        startActivity(intent)
    }

}
