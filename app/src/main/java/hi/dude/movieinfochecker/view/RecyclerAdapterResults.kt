package hi.dude.movieinfochecker.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.ResultItem
import kotlinx.android.synthetic.main.list_item_result.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapterResults(results: ArrayList<ResultItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var results: ArrayList<ResultItem> = results
        set(value) {
            field = value
            notifyDataSetChanged()
            pullImages()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RecyclerAdapterMovie.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_result, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = results[position]
        val view = holder.itemView

        if (result.imageBitmap == null)
            view.ivPosterResult.setImageResource(R.drawable.empty)
        else
            view.ivPosterResult.setImageBitmap(result.imageBitmap)

//        result.setOnImageChangeListener { notifyItemChanged(position) }

        view.tvTitleResult.text = result.title ?: ""
        view.tvDescription.text = result.description ?: ""
    }

    fun pullImages() {
        for (position in results.indices) {
            try {
                val job = CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                    results[position].imageBitmap = Picasso.get()
                        .load(results[position].imageUrl)
                        .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
                        .error(R.drawable.empty)
                        .placeholder(R.drawable.empty)
                        .get()
                    withContext(Dispatchers.Main) { notifyItemChanged(position) }
                }
                job.invokeOnCompletion { t ->
                    if (t != null) Log.e(
                        "ResultsAdapter",
                        "pullImages: invokeOnCompletion ",
                        t
                    )
                }
            } catch (e: Throwable) {
                Log.e("ResultsAdapter", "pullImages: ", e)
            }

        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}