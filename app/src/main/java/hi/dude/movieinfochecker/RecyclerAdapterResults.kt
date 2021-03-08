package hi.dude.movieinfochecker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hi.dude.movieinfochecker.models.ResultItem
import kotlinx.android.synthetic.main.list_item_result.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapterResults(results: ArrayList<ResultItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var results: ArrayList<ResultItem> = results
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
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

        view.tvTitleResult.text = result.title ?: ""
        view.tvDescription.text = result.description ?: ""
    }

    override fun getItemCount(): Int = results.size

    suspend fun pullImages() = withContext(Dispatchers.IO) {
        for (position in 0 until results.size) launch {
            results[position].pullImage()
            withContext(Dispatchers.Main) { notifyItemChanged(position) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}