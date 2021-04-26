package hi.dude.movieinfochecker.view.main.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.android.synthetic.main.list_item_hint.view.*
import javax.inject.Inject

class HintsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var hints = ArrayList<ResultItem>()
        set(value) {
            field =
                if (value.size > 6) ArrayList(value.subList(0, 6))
                else value
            notifyDataSetChanged()
        }

    var onHintClicked: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_hint, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hint = hints[position]

        holder.itemView.apply {
            tvHint.text = hint.title?.replace('-', ' ')
            setOnClickListener { onHintClicked(hint.title ?: "") }
        }
    }

    override fun getItemCount(): Int = hints.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}