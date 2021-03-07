package hi.dude.movieinfochecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.*

class MovieListActivity : AppCompatActivity() {

    private lateinit var vpAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        supportActionBar?.hide()
        setUpPager()
    }

    private fun setUpPager() {
        val popular = Page(ArrayList(), searchContainer)
        val top = Page(ArrayList(), searchContainer)

        vpAdapter = PagerAdapter(arrayListOf(popular, top))
        vpMovies.adapter = vpAdapter
        pullMovieList()
    }

    private fun pullMovieList() = CoroutineScope(Dispatchers.Main).launch {
        val movies = withContext(Dispatchers.IO) { App.connector.getMostPopularMovies() }
        vpAdapter.pages[vpMovies.currentItem].movies = movies
    }
}