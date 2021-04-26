package hi.dude.movieinfochecker.view.main.fragments.movielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.application.App
import kotlinx.android.synthetic.main.movie_list_fragment.*
import javax.inject.Inject

class MovieListFragment : Fragment() {

    @Inject
    lateinit var viewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity?.application as App).component.inject(this)

        viewModel.bindAdapter(rvMovieList, context)
        viewModel.movieList.observe(viewLifecycleOwner) {
            viewModel.bindAdapter(rvMovieList, context)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateRecyclerContent()
    }
}