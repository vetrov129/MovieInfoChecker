package hi.dude.movieinfochecker.view

import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Actor
import kotlinx.android.synthetic.main.list_item_actors.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapterActor(actors: List<Actor>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var actors = actors
        set(value) {
            field = value
            Log.i("ActorAdapter", "setter actors: ok")
            notifyDataSetChanged()
            pullImages()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_actors, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val actor = actors[position]
        val view = holder.itemView

        view.tvActorName.text = actor.name
        view.tvRole.text = actor.asCharacter

        if (actors[position].bitmap != null)
            view.ivActorPhoto.setImageBitmap(actors[position].bitmap)
        else
            view.ivActorPhoto.setImageResource(R.drawable.empty)
    }

    override fun getItemCount(): Int = actors.size

    private fun pullImages() {
        try {
            for (position in actors.indices) {
                val job = CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                    actors[position].bitmap = Picasso.get()
                        .load(actors[position].imageUrl)
                        .placeholder(R.drawable.empty)
                        .error(R.drawable.empty)
                        .resizeDimen(R.dimen.actor_photo_width, R.dimen.actor_photo_height)
                        .centerCrop()
                        .get()
                    withContext(Dispatchers.Main) { notifyItemChanged(position) }
                }
                job.invokeOnCompletion { t ->
                    if (t != null) Log.e(
                        "ActorAdapter",
                        "pullImages: invokeOnCompletion ",
                        t
                    )
                }
                Log.i("ActorAdapter", "pullImages: ok")
            }
        } catch (e: Throwable) {
            Log.e("ActorAdapter", "pullImages: ", e)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}