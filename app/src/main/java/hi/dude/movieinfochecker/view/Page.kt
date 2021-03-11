package hi.dude.movieinfochecker.view

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.viewmodel.MovieListViewModel

class Page(
    movies: ArrayList<Movie>,
    val searchPanel: View,
    resources: Resources,
    context: Context,
    val viewModel: MovieListViewModel
) {

    private var readyToHide = true

    private val recAdapter: RecyclerAdapterMovie = RecyclerAdapterMovie(movies, resources, context, viewModel)

    var movies: ArrayList<Movie> = movies
        set(value) {
            field = value
            recAdapter.movies = value
        }

    lateinit var recycler: RecyclerView

    fun bind(recycler: RecyclerView) {
        this.recycler = recycler
        recycler.adapter = recAdapter
        setListeners()
    }

    private fun setListeners() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (readyToHide) {
                    if (dy < 0) {
                        searchPanel.visibility = View.VISIBLE
                        readyToHide = false
                    }
                    if (dy > 3) {
                        searchPanel.visibility = View.GONE
                        readyToHide = false
                    }
                }
                readyToHide =
                    recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE ||
                            recyclerView.scrollState != RecyclerView.SCROLL_STATE_DRAGGING
            }
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val manager = recycler.layoutManager as LinearLayoutManager
                    if (manager.findLastVisibleItemPosition() >= recAdapter.countOfPacks * recAdapter.packSize - 5) {
                        viewModel.pullPosters(
                            recAdapter.countOfPacks * recAdapter.packSize,
                            (recAdapter.countOfPacks + 1) * recAdapter.packSize,
                            recAdapter,
                            movies
                        )
                        recAdapter.countOfPacks++
                    }
                }
            }
        })
    }
}