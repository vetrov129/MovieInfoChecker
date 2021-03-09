package hi.dude.movieinfochecker.model

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import kotlin.coroutines.CoroutineContext

class MovieRepository {

    private val connector = ApiConnector()

    val popularMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    val topMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    val searchResult: MutableLiveData<ArrayList<ResultItem>> = MutableLiveData()

    init {
        popularMovies.value = ArrayList()
        topMovies.value = ArrayList()
        searchResult.value = ArrayList()
    }

    suspend fun pullPopular(mainContext: CoroutineContext) = withContext(Dispatchers.IO) {
        val movies = connector.getMostPopularMovies()
        withContext(mainContext) { popularMovies.value = movies }
    }

    suspend fun pullTop(mainContext: CoroutineContext) = withContext(Dispatchers.IO) {
        val movies = connector.getTop250Movies()
        withContext(mainContext) { topMovies.value = movies }
    }

    suspend fun search(query: String, coroutineContext: CoroutineContext) = withContext(Dispatchers.IO) {
        val result = connector.search(query)
        withContext(coroutineContext) { searchResult.value = result }
    }

    fun clearResult() {
        searchResult.value?.clear()
    }
}