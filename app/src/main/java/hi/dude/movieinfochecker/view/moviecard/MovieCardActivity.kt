package hi.dude.movieinfochecker.view.moviecard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.application.App
import kotlinx.android.synthetic.main.activity_movie_card.*
import javax.inject.Inject

class MovieCardActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MovieCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_card)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.colorAutoDark)
        (application as App).component.inject(this)

        val id = intent.getStringExtra("id") ?: ""
        val textViews =
            MovieTextViews(tvTitleCard, tvDateCard, tvCountriesCard, tvDurationCard, tvPlotCard, tvSimilarCard)

        viewModel.loadMovieInfo(id)
        viewModel.bindGeneral(rvActorsCard, rvSimilarCard, ivStarCard, this)
        viewModel.movieInfoStack.observe(this) {
            viewModel.updateData(
                textViews,
                ivCardPoster,
                ivStarCard,
                cardContainer
            )
        }
    }

    override fun onBackPressed() {
        if (viewModel.toBack())
            super.onBackPressed()
    }

    data class MovieTextViews(
        val title: TextView,
        val date: TextView,
        val countries: TextView,
        val duration: TextView,
        val plot: TextView,
        val similarLabel: TextView
    )
}