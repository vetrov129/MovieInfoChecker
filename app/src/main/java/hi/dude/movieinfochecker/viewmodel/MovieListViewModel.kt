package hi.dude.movieinfochecker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.model.MovieRepository
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.model.entities.MovieInfo
import hi.dude.movieinfochecker.model.entities.ResultItem
import hi.dude.movieinfochecker.model.entities.WithPoster
import kotlinx.coroutines.*

class MovieListViewModel private constructor(app: Application) : AndroidViewModel(app), CoroutineScope {

    companion object {
        private var viewModel: MovieListViewModel? = null

        fun getInstance(app: Application): MovieListViewModel {
            if (viewModel == null)
                viewModel = MovieListViewModel(app)
            return viewModel!!
        }
    }

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

//    private val imageScope: CoroutineScope = CoroutineScope(Dispatchers.IO) + SupervisorJob()

    private val repository: MovieRepository = MovieRepository()

    private var searchJob: Job? = null

    val popularMovies: LiveData<ArrayList<Movie>> = repository.popularMovies
    val topMovies: LiveData<ArrayList<Movie>> = repository.topMovies
    val searchResult: LiveData<ArrayList<ResultItem>> = repository.searchResult

    val currentMovie: LiveData<MovieInfo> = repository.currentMovie

    private val handlerShort = CoroutineExceptionHandler { _, exception ->
        println("EXCEPTION/VIEWMODEL: Caught $exception}")
    }

    private val handlerLong = CoroutineExceptionHandler { _, exception ->
        println("EXCEPTION/VIEWMODEL: Caught ${exception.printStackTrace()}}")
    }

    fun pullPopular() {
        try {
            if (popularMovies.value?.size != 0) return
            launch(handlerLong) { repository.pullPopular(coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun pullTop() {
        try {
            if (topMovies.value?.size != 0) return
            launch(handlerLong) { repository.pullTop(coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun search(query: String) {
        try {
            searchJob = launch(handlerLong) { repository.search(query, coroutineContext) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun pullMovieInfo(id: String) {
        try {
            launch(handlerLong) {
                repository.pullMovieInfo(id)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun clearResult() {
        repository.clearResult()
    }

    fun clearCurrentMovie() {
        repository.clearCurrentMovie()
    }

    fun cancel() {
        job.cancel()
    }

    fun pullPosters(start: Int, end: Int, adapter: RecyclerView.Adapter<*>, list: List<WithPoster>) {
        for (position in start until end) {
            launch(handlerLong) {
                try {
                    list[position].pullPoster()
                    adapter.notifyItemChanged(position)
                } catch (e: IndexOutOfBoundsException) {
                    Log.e("ViewModel", "pullImages: end of list")
                }

            }
        }
    }

}