package hi.dude.movieinfochecker.view.main.fragments.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.application.App
import kotlinx.android.synthetic.main.search_fragment.*
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity?.application as App).component.inject(this)

        viewModel.bindGeneral(rvResult, rvHints, rgSearchCategory, context!!)
        viewModel.bindSearch(etSearch, ibRunSearch,
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        viewModel.hints.observe(viewLifecycleOwner) { viewModel.onHintsChanged(etSearch) }
        viewModel.results.observe(viewLifecycleOwner) { viewModel.onResultsChanged() }
    }
}