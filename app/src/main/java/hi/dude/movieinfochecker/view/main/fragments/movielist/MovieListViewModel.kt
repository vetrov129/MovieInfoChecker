package hi.dude.movieinfochecker.view.main.fragments.movielist

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.MovieShort
import hi.dude.movieinfochecker.model.repository.Repository
import hi.dude.movieinfochecker.view.moviecard.MovieCardActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class MovieListViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "MovieListViewModel"

        const val packSize = 20
        const val offsetToScrollLoad = 7
    }
    var countOfPacks = 1

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var listAdapter: MovieListRecyclerAdapter

    @Inject
    lateinit var handler: CoroutineExceptionHandler
    lateinit var movieList: LiveData<ArrayList<MovieShort>>

    private val delayedUIUpdates = ArrayList<() -> Unit>()

    init {
        Log.i(TAG, "init")
    }

    fun bindAdapter(recycler: RecyclerView, context: Context?) {
        initMovieList()
        recycler.adapter = listAdapter
        listAdapter.movies = ArrayList()
        addItemsToList(recycler, 0, packSize)
        setListener(recycler)

        listAdapter.onMovieClicked = {
            val intent = Intent(context, MovieCardActivity::class.java)
            intent.putExtra("id", it)
            context?.startActivity(intent)
        }
        listAdapter.isFavor = { isFavor(it ?: "") }
        listAdapter.onStarClicked = { movie, position ->
            viewModelScope.launch (handler) {
                if (isFavor(movie.id))
                    repository.removeFavor(movie)
                else
                    repository.addFavor(movie)
                withContext(Dispatchers.Main) { listAdapter.notifyItemChanged(position) }
            }
        }
    }

    fun updateRecyclerContent() {
        listAdapter.notifyDataSetChanged()
    }

    protected open fun initMovieList() {
        if (!::movieList.isInitialized) {
            movieList = repository.moviesList
            viewModelScope.launch(handler) { repository.pullMoviesList() }
        }
    }

    private fun setListener(recycler: RecyclerView) =
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager

                    if (manager.findLastVisibleItemPosition() >= countOfPacks * packSize - offsetToScrollLoad) {
                        addItemsToList(recycler, countOfPacks * packSize, packSize)
                        countOfPacks++
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updateDelayed(recyclerView)
            }
        })

    fun updateDelayed(recycler: RecyclerView) {
        if (recycler.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            try {
                delayedUIUpdates.forEach { it.invoke() }
                delayedUIUpdates.clear()
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "updateDelayed: IndexOutOfBoundsException")
            }
        }
    }

    fun addItemsToList(recycler: RecyclerView, start: Int, count: Int) {
        Log.i(TAG, "addItemsToList: $countOfPacks packs ${start + 1}-${start + count} ")
        if (movieList.value == null) return
        for (position in start until (start + count)) {
            try {
                listAdapter.movies.add(movieList.value!![position])
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "addItemsToList: end of list")
            }
        }
        delayedUIUpdates.add { listAdapter.notifyItemRangeInserted(start, count) }
        updateDelayed(recycler)
        loadImages(recycler, start, count)
    }

    private fun loadImages(recycler: RecyclerView, start: Int, count: Int) {
        if (movieList.value == null) return
        for (position in start until (start + count)) {
            val loadJob = viewModelScope.launch(handler) {
                try {
                    loadImage(movieList.value!![position])
                } catch (e: IndexOutOfBoundsException) {
                    Log.e(TAG, "loadImages: IndexOutOfBoundsException ${e.message}")
                }
            }
            viewModelScope.launch(handler) {
                loadJob.join()
                delayedUIUpdates.add { listAdapter.notifyItemChanged(position) }
                updateDelayed(recycler)
            }
        }
    }

    private suspend fun loadImage(movie: MovieShort) = withContext(Dispatchers.IO) {
        movie.bitmap = Picasso.get()
            .load(movie.imageUrl)
            .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
            .onlyScaleDown()
            .get()
    }

    private fun isFavor(id: String): Boolean = repository.favorSet.contains(id)

}
