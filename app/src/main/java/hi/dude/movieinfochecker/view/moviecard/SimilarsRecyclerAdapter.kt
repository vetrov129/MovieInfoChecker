package hi.dude.movieinfochecker.view.moviecard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.MovieShort
import kotlinx.android.synthetic.main.list_item_similar.view.*
import javax.inject.Inject

class SimilarsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies = ArrayList<MovieShort>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onMovieClicked: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_similar, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]

        holder.itemView.apply {
            ivPosterSimilar.setImageBitmap(movie.bitmap)
            tvTitleSimilar.text = cutText(movie.title ?: "")
            tvYearSimilar.text = movie.year

            setOnClickListener { onMovieClicked(movie.id ?: "") }
        }
    }

    private fun cutText(text: String): String {
        return if (text.length > 14)
            "${text.substring(0..14)}..."
        else
            text
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}