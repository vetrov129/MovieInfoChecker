package hi.dude.movieinfochecker.view.moviecard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Actor
import kotlinx.android.synthetic.main.list_item_actors.view.*
import javax.inject.Inject

class ActorsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var actors = ArrayList<Actor>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onActorClicked: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_actors, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val actor = actors[position]

        holder.itemView.apply {
            ivActorPhoto.setImageBitmap(actor.bitmap)
            tvActorName.text = cutText(actor.name ?: "")
            tvRole.text = cutText(actor.asCharacter ?: "")

            setOnClickListener { onActorClicked(actor.id ?: "") }
        }
    }

    private fun cutText(text: String): String {
        return if (text.length > 14)
            "${text.substring(0..14)}..."
        else
            text
    }

    override fun getItemCount(): Int = actors.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}