package hi.dude.movieinfochecker.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class RecyclerAdapterMovie(movies: ArrayList<Movie>, private val resources: Resources) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies: ArrayList<Movie> = movies
        set(value) {
            field = value
            notifyDataSetChanged()
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

        movie.setOnImageChangeListener { notifyItemChanged(position) }

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

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}