package hi.dude.movieinfochecker.view.moviecard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.HasImage
import hi.dude.movieinfochecker.model.entities.MovieLong
import hi.dude.movieinfochecker.model.entities.MovieShort
import hi.dude.movieinfochecker.model.repository.Repository
import hi.dude.movieinfochecker.model.repository.StackLiveData
import hi.dude.movieinfochecker.view.personcard.PersonCardActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MovieCardViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "MovieCardViewModel"
    }

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var handler: CoroutineExceptionHandler

    @Inject
    lateinit var similarsAdapter: SimilarsRecyclerAdapter

    @Inject
    lateinit var actorsAdapter: ActorsRecyclerAdapter
    lateinit var movieInfoStack: StackLiveData<MovieLong>

    fun toNext(id: String) {
        viewModelScope.launch { repository.pullMovieInfo(id) }
    }

    fun toBack(): Boolean {
        movieInfoStack.pop()
        return movieInfoStack.isEmpty()
    }

    fun scrollUp(container: NestedScrollView) {
        container.smoothScrollTo(0, container.top, 1200)
    }

    fun bindGeneral(actors: RecyclerView, similars: RecyclerView, star: ImageView, context: Activity) {
        movieInfoStack = repository.movieInfoStack
        similarsAdapter.onMovieClicked = { toNext(it) }
        actorsAdapter.onActorClicked = { onActorClicked(it, context) }
        actors.adapter = actorsAdapter
        similars.adapter = similarsAdapter

        star.setOnClickListener { onStarClicked(star) }

    }

    private fun bindText(views: MovieCardActivity.MovieTextViews) {
        views.apply {
            title.text = movieInfoStack.peek()?.title ?: ""
            countries.text = movieInfoStack.peek()?.countries ?: ""
            duration.text = movieInfoStack.peek()?.duration ?: ""
            plot.text = movieInfoStack.peek()?.plot ?: ""
            similarLabel.text = if (movieInfoStack.isEmpty()) "" else "Similar"
            try {
                date.text = LocalDate.parse(movieInfoStack.peek()?.date)
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
            } catch (e: Exception) {
                Log.i(TAG, "bindText: ", e)
            }
        }

    }

    fun updateData(
        textViews: MovieCardActivity.MovieTextViews,
        poster: ImageView,
        star: ImageView,
        container: NestedScrollView
    ) {
        poster.setImageResource(R.color.colorBlack)
        updateStar(star)

        actorsAdapter.actors = movieInfoStack.peek()?.actorList ?: ArrayList()
        similarsAdapter.movies = movieInfoStack.peek()?.similars ?: ArrayList()
        loadImagesToRecyclers()

        if (movieInfoStack.peek()?.imageUrl != null) {
            bindText(textViews)
            try {
                Log.i(TAG, "updateData: load image ${movieInfoStack.peek()?.imageUrl}")
                Picasso.get()
                    .load(movieInfoStack.peek()!!.imageUrl)
                    .placeholder(R.color.colorBlack)
                    .resize(poster.width, poster.height)
                    .centerCrop()
                    .into(poster)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "updateData: IllegalArgumentException")
            }
        }
        scrollUp(container)
    }

    private fun updateStar(star: ImageView) {
        star.setImageResource(
            if (isFavor(movieInfoStack.peek()?.id ?: ""))
                R.drawable.ic_filled_star_white
            else
                R.drawable.ic_empty_star_white
        )
    }

    private fun onStarClicked(star: ImageView) {
        val movie = movieInfoStack.value?.peek() ?: return
        val movieShort = MovieShort(movie.id ?: "", movie.title, movie.year, movie.imageUrl, movie.rating)

        viewModelScope.launch(handler) {
            if (isFavor(movie.id ?: ""))
                repository.removeFavor(movieShort)
            else
                repository.addFavor(movieShort)
            updateStar(star)
        }
    }

    private fun onActorClicked(id: String, context: Activity) {
        val intent = Intent(context, PersonCardActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
        context.finish()
    }

    private fun loadImagesToRecyclers() {
        loadImages(actorsAdapter, actorsAdapter.actors)
        loadImages(similarsAdapter, similarsAdapter.movies)
    }

    private fun loadImages(adapter: RecyclerView.Adapter<*>, list: List<HasImage>) {
        list.forEachIndexed { position, item ->
            val loadJob = viewModelScope.launch(handler) {
                withContext(Dispatchers.IO) {
                    item.bitmap = loadImage(item.imageUrl)
                }
            }
            viewModelScope.launch(handler) {
                loadJob.join()
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun loadImage(url: String?): Bitmap =
        Picasso.get()
            .load(url)
            .resizeDimen(R.dimen.actor_photo_width, R.dimen.actor_photo_height)
            .centerCrop()
            .get()

    fun loadMovieInfo(id: String) = viewModelScope.launch { repository.pullMovieInfo(id) }

    fun isFavor(id: String) = repository.favorSet.contains(id)

    fun clearMovieInfo() = repository.movieInfoStack.clear()
}