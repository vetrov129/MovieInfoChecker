package hi.dude.movieinfochecker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hi.dude.movieinfochecker.model.MovieRepository
import hi.dude.movieinfochecker.model.entities.Movie
import kotlinx.coroutines.*
import timber.log.Timber

class MovieListViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    private val repository: MovieRepository = MovieRepository()

    val popularMovies: MutableLiveData<ArrayList<Movie>> = repository.popularMovies
    val topMovies: MutableLiveData<ArrayList<Movie>> = repository.topMovies

    var posterWidth = 0
    var posterHeight = 0

    fun pullPopular() {
        try {
            if (popularMovies.value?.size != 0) return
            launch { repository.pullPopular(coroutineContext) }
                .invokeOnCompletion { launch { repository.pullPosters(popularMovies, posterWidth, posterHeight) } }
        } catch (e: Throwable) {
            Timber.e(e)
            println("pullPopular ${e.message}")
            e.printStackTrace()
        }

    }

    fun pullTop() {
        try {
            if (topMovies.value?.size != 0) return
            launch { repository.pullTop(coroutineContext) }
                .invokeOnCompletion { launch { repository.pullPosters(topMovies, posterWidth, posterHeight) } }
        } catch (e: Throwable) {
            Timber.e(e)
            println("pullTop ${e.message}")
            e.printStackTrace()
        }
    }

    fun cancel() {
        job.cancel()
    }

}