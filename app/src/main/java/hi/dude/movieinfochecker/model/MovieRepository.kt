package hi.dude.movieinfochecker.model

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import androidx.lifecycle.MutableLiveData
import hi.dude.movieinfochecker.model.ApiConnector
import hi.dude.movieinfochecker.model.entities.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.CoroutineContext

class MovieRepository() {

    private val connector = ApiConnector()

    val popularMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    val topMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()

    init {
        popularMovies.value = ArrayList()
        topMovies.value = ArrayList()
    }

    suspend fun pullPopular(mainContext: CoroutineContext) = withContext(Dispatchers.IO) {
        val movies = connector.getMostPopularMovies()
        withContext(mainContext) { popularMovies.value = movies }
    }

    suspend fun pullTop(mainContext: CoroutineContext) = withContext(Dispatchers.IO) {
        val movies = connector.getTop250Movies()
        withContext(mainContext) { topMovies.value = movies }
    }

    suspend fun pullPosters(list: MutableLiveData<ArrayList<Movie>>, width: Int, height: Int) =
        withContext(Dispatchers.IO) {
            if (list.value == null) return@withContext
            for (movie in list.value!!) {
                println("pullPosters: ${movie.imageUrl}")
                launch { pullImage(movie, width, height) }
            }
        }

    private suspend fun pullImage(movie: Movie, width: Int, height: Int) {
        try {
            val connection = URL(movie.imageUrl).openConnection()
            connection.doInput = true
            connection.connect()
            movie.imageBitmap = BitmapFactory.decodeStream(connection.getInputStream()).scale(width, height)
            movie.execOnChange(Dispatchers.Main) // TODO: 09.03.2021 здесь этого не должно быть мб в ViewModel
        } catch (e: Throwable) {
            println("pullImage: url ${movie.imageUrl} error ${e.message}")
        }
    }
}