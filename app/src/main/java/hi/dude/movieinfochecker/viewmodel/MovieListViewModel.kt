package hi.dude.movieinfochecker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hi.dude.movieinfochecker.model.MovieRepository
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.coroutines.*

class MovieListViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    private val repository: MovieRepository = MovieRepository()

    private var searchJob: Job? = null

    val popularMovies: MutableLiveData<ArrayList<Movie>> = repository.popularMovies
    val topMovies: MutableLiveData<ArrayList<Movie>> = repository.topMovies
    val searchResult: MutableLiveData<ArrayList<ResultItem>> = repository.searchResult

    fun pullPopular() {
        try {
            if (popularMovies.value?.size != 0) return
            launch { repository.pullPopular(coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun pullTop() {
        try {
            if (topMovies.value?.size != 0) return
            launch { repository.pullTop(coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun search(query: String) {
        try {
            searchJob = launch { repository.search(query, coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun clearResult() {
        repository.clearResult()
    }

    fun cancel() {
        job.cancel()
    }

}