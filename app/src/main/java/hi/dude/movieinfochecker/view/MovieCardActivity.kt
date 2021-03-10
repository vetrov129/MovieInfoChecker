package hi.dude.movieinfochecker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.MovieInfo
import hi.dude.movieinfochecker.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.activity_movie_card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieCardActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel
    private lateinit var id: String
    private val actorsAdapter = RecyclerAdapterActor(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_card)

        supportActionBar?.hide()
        id = intent.getStringExtra("id") ?: ""

        viewModel = MovieListViewModel.getInstance(application)
        viewModel.pullMovieInfo(id)

        rvActorsCM.adapter = actorsAdapter
        setContent(viewModel.currentMovie.value!!)
        subscribe()
    }

    override fun onStop() {
        viewModel.clearCurrentMovie()
        super.onStop()
    }

    private fun setContent(info: MovieInfo) {
        if (info.imageUrl != "") Picasso.get()
            .load(info.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.empty)
            .error(R.drawable.empty)
            .fit()
            .into(ivPosterCM)

        tvTitleCM.text = info.title
        tvYearCM.text = info.year
        tvDurationCM.text = info.runtimeStr
        tvDirectorsCM.text = info.directors
        tvStarsCM.text = info.stars
        tvGenresCM.text = info.genres
        tvPlotCM.text = info.plot

        Log.i("CardActivity", "setContent: actors ${info.actorList?.size} ")

        actorsAdapter.actors = info.actorList ?: ArrayList()
    }

    private fun subscribe() {
        viewModel.currentMovie.observe(this) {
            setContent(viewModel.currentMovie.value!!)
        }
    }
}