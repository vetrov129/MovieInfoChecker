package hi.dude.movieinfochecker.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Movie
import hi.dude.movieinfochecker.model.entities.WithPoster
import hi.dude.movieinfochecker.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class RecyclerAdapterMovie(
    movies: ArrayList<Movie>,
    private val resources: Resources,
    private val context: Context,
    private val viewModel: MovieListViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies: ArrayList<Movie> = movies
        set(value) {
            field = value
            notifyDataSetChanged()
            viewModel.pullPosters(0, packSize, this, movies)
        }

    var countOfPacks = 1
    val packSize = 20

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

        if (movie.rating != null) {
            if (movie.rating == "") view.cardRating.visibility = View.GONE
            else when {
                movie.rating.toDouble() >= 6.5 ->
                    view.cardRating.setCardBackgroundColor(resources.getColor(R.color.colorGreen))
                movie.rating.toDouble() < 6.5 && movie.rating.toDouble() >= 4.5 ->
                    view.cardRating.setCardBackgroundColor(resources.getColor(R.color.colorGrayRating))
                movie.rating.toDouble() < 4.5 ->
                    view.cardRating.setCardBackgroundColor(resources.getColor(R.color.colorRed))
            }
        }

        view.setOnClickListener { movieClicked(position) }
    }

    private fun movieClicked(position: Int) {
        val intent = Intent(context, MovieCardActivity::class.java)
        viewModel.clearCurrentMovie()
        intent.putExtra("id", movies[position].id)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}