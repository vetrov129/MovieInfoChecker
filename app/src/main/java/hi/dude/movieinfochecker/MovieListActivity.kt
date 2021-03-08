package hi.dude.movieinfochecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.*

class MovieListActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var vpAdapter: PagerAdapter

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        supportActionBar?.hide()

        App.imageWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128f, resources.displayMetrics).toInt()
        App.imageHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 176f, resources.displayMetrics).toInt()
        App.resources = resources
        setUpPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun setUpPager() {
        val popular = Page(ArrayList(), searchContainer)
        val top = Page(ArrayList(), searchContainer)

        vpAdapter = PagerAdapter(arrayListOf(popular, top))
        vpMovies.adapter = vpAdapter
        pullMovieList()
    }

    private fun pullMovieList() = launch {
        val movies = withContext(Dispatchers.IO) { App.connector.getMostPopularMovies() }
        vpAdapter.pages[vpMovies.currentItem].apply {
            this.movies = movies
            pullImages()
        }
    }
}