package hi.dude.movieinfochecker.model

import androidx.lifecycle.MutableLiveData
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.model.entities.MovieInfo
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MovieRepository {

    private val connector = ApiConnector()

    val popularMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    val topMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    val searchResult: MutableLiveData<ArrayList<ResultItem>> = MutableLiveData()

    val currentMovie: MutableLiveData<MovieInfo> = MutableLiveData()

    init {
        popularMovies.value = ArrayList()
        topMovies.value = ArrayList()
        searchResult.value = ArrayList()
        currentMovie.value = MovieInfo()
    }

    suspend fun pullPopular(mainContext: CoroutineContext) = withContext(mainContext) {
        popularMovies.value = connector.getMostPopularMovies()
    }

    suspend fun pullTop(mainContext: CoroutineContext) = withContext(mainContext) {
        topMovies.value = connector.getTop250Movies()
    }

    suspend fun search(query: String, mainContext: CoroutineContext) = withContext(mainContext) {
        searchResult.value = connector.search(query)
    }

    suspend fun pullMovieInfo(id: String) {
        currentMovie.value = connector.getInfoById(id)
    }

    fun clearResult() {
        searchResult.value?.clear()
    }

    fun clearCurrentMovie() {
        currentMovie.value = MovieInfo()
    }
}