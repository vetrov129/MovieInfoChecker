package hi.dude.movieinfochecker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.models.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class RecyclerAdapterMovie(movies: ArrayList<Movie>) :
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

//        view.ivPoster.setImageBitmap(movie.imageBitmap)
        view.tvTitle.text = movie.title ?: ""
        view.tvCrew.text = movie.crew ?: ""
        view.tvGrowth.text = movie.growth ?: ""
        view.tvRating.text = movie.rating.toString() ?: ""
        view.tvNumber.text = movie.rank.toString() ?: ""

//        if (position % 2 == 0) view.setBackgroundResource(R.color.colorBG)
//        else view.setBackgroundResource(R.color.colorAccent)

        // TODO: 07.03.2021 менять цвет и стрелку в зависимости от роста
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}