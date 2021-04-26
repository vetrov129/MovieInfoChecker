package hi.dude.movieinfochecker.view.main.fragments.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.android.synthetic.main.list_item_result.view.*
import javax.inject.Inject

class ResultRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var results = ArrayList<ResultItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClicked: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_result, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = results[position]

        holder.itemView.apply {
            ivPosterResult.setImageBitmap(item.bitmap)
            tvTitleResult.text = cutText(item.title ?: "")
            setOnClickListener { onItemClicked(item.id) }
        }
    }

    private fun cutText(text: String): String {
        return if (text.length > 20)
            "${text.substring(0..20)}..."
        else
            text
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}