package hi.dude.movieinfochecker.view.personcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.PersonMovie
import kotlinx.android.synthetic.main.list_item_similar.view.*
import javax.inject.Inject

class MovieRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies = ArrayList<PersonMovie>()
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
            tvYearSimilar.text = cutText(movie.role ?: "")
            setOnClickListener { onMovieClicked(movie.id) }
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