package hi.dude.movieinfochecker.view

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class RecyclerAdapterMovie(movies: ArrayList<Movie>, private val resources: Resources) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies: ArrayList<Movie> = movies
        set(value) {
            field = value
            notifyDataSetChanged()
            pullImages()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]
        val view = holder.itemView

        if (movie.imageBitmap == null)
            view.ivPoster.setImageResource(R.drawable.empty)
        else
            view.ivPoster.setImageBitmap(movie.imageBitmap)

        view.tvTitle.text = movie.title ?: ""
        view.tvCrew.text = movie.crew ?: ""
        view.tvYear.text = movie.year
        view.tvGrowth.text = movie.growth ?: ""
        view.tvRating.text = movie.rating.toString() ?: ""
        view.tvNumber.text = movie.rank.toString() ?: ""

        if (movie.growth != null) {
            when {
                movie.growth == "0" -> view.tvGrowth.text = ""
                movie.growth[0] == '-' -> view.tvGrowth.setTextColor(resources.getColor(R.color.colorRed))
                else -> view.tvGrowth.setTextColor(resources.getColor(R.color.colorGreen))
            }
        }
    }

    fun pullImages() {
        for (position in movies.indices) {
            try {
                val job = CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                    movies[position].imageBitmap = Picasso.get()
                        .load(movies[position].imageUrl)
                        .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
                        .error(R.drawable.empty)
                        .placeholder(R.drawable.empty)
                        .get()
                    withContext(Dispatchers.Main) { notifyItemChanged(position) }
                }
                job.invokeOnCompletion { t ->
                    if (t != null) Log.e(
                        "MovieAdapter",
                        "pullImages: invokeOnCompletion ",
                        t
                    )
                }

            } catch (e: Throwable) {
                Log.e("MoviesAdapter", "pullImages: ", e)
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}