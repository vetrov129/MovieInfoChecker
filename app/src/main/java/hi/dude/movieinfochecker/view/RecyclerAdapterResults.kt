package hi.dude.movieinfochecker.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.ResultItem
import hi.dude.movieinfochecker.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.list_item_result.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapterResults(
    results: ArrayList<ResultItem>,
    private val context: Context,
    private val viewModel: MovieListViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var results: ArrayList<ResultItem> = results
        set(value) {
            field = value
            notifyDataSetChanged()
            viewModel.pullPosters(0, results.size, this, results)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
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

        view.setOnClickListener { movieClicked(position) }
    }

    private fun movieClicked(position: Int) {
        val intent = Intent(context, MovieCardActivity::class.java)
        viewModel.clearCurrentMovie()
        intent.putExtra("id", results[position].id)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}