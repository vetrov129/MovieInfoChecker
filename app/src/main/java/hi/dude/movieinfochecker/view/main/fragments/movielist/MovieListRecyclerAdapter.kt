package hi.dude.movieinfochecker.view.main.fragments.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.MovieShort
import kotlinx.android.synthetic.main.list_item_movie.view.*
import javax.inject.Inject

class MovieListRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies: ArrayList<MovieShort> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onMovieClicked: (String?) -> Unit = {}
    var onStarClicked: (MovieShort, Int) -> Unit = { _, _ -> }
    var isFavor: (String?) -> Boolean = { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]

        holder.itemView.apply {
            ivPoster.setImageBitmap(movie.bitmap)
            tvTitle.text = movie.title
            tvYear.text = movie.year
            tvRating.text = movie.rating
            cardRating.setCardBackgroundColor(resources.getColor(getRatingColorId(movie.rating ?: "0")))

            ivStar.setImageResource(
                if (isFavor(movie.id))
                    R.drawable.ic_filled_star
                else
                    R.drawable.ic_empty_star
            )

            setOnClickListener { onMovieClicked(movie.id) }
            ivStar.setOnClickListener { onStarClicked(movie, position) }
        }
    }

    private fun getRatingColorId(rating: String): Int {
        val ratingF = try {
            rating.toFloat()
        } catch (e: NumberFormatException) {
            0f
        } catch (e: NullPointerException) {
            0f
        }
        return when {
            ratingF >= 6.5 -> R.color.colorGreen
            ratingF < 6.5 && ratingF >= 4.5 -> R.color.colorGrayRating
            ratingF < 4.5 && ratingF != 0f -> R.color.colorRed
            else -> R.color.colorTransparent
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}