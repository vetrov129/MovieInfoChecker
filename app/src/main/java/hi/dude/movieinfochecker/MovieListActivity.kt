package hi.dude.movieinfochecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie_list.*

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

    private fun pullMovieList() = Thread {
        val movies = App.connector.getMostPopularMovies()
        runOnUiThread { vpAdapter.pages[0].movies = movies }
    }.start()
}