package hi.dude.movieinfochecker.view.main.fragments.favorites

import android.util.Log
import hi.dude.movieinfochecker.view.main.fragments.movielist.MovieListViewModel
import javax.inject.Inject

class FavoritesViewModel @Inject constructor() : MovieListViewModel() {

    companion object {
        const val TAG = "FavoritesViewModel"
    }

    init {
        Log.i(TAG, "init")
    }

    override fun initMovieList() {
        movieList = repository.favorList
    }
}