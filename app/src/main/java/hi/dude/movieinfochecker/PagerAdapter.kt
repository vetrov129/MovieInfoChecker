package hi.dude.movieinfochecker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PagerAdapter(val pages: ArrayList<Page>) :
    RecyclerView.Adapter<PagerAdapter.PagerVH>() {

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.page_movie_list, parent, false)
        return PagerVH(itemView)
    }

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        pages[position].bind(holder.itemView.findViewById(R.id.rvMovieList))
    }

    override fun getItemCount(): Int = pages.size
}