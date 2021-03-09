package hi.dude.movieinfochecker.view

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.model.entities.Movie

class Page(movies: ArrayList<Movie>, val searchPanel: View, resources: Resources) {

    private var readyToHide = true

    private val recAdapter: RecyclerAdapterMovie = RecyclerAdapterMovie(movies, resources)

    var movies: ArrayList<Movie> = movies
        set(value) {
            field = value
            recAdapter.movies = value
        }

    lateinit var recycler: RecyclerView

    fun bind(recycler: RecyclerView) {
        this.recycler = recycler
        recycler.adapter = recAdapter
        setListener()
    }

    private fun setListener() {
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
    }
}