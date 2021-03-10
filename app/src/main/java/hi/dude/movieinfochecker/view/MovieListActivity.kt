package hi.dude.movieinfochecker.view

import android.animation.AnimatorInflater
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import hi.dude.movieinfochecker.viewmodel.MovieListViewModel
import hi.dude.movieinfochecker.R
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.Dispatchers

class MovieListActivity : AppCompatActivity() {

    private lateinit var popularPage: Page
    private lateinit var topPage: Page

    private lateinit var vpAdapter: PagerAdapter
    private lateinit var rvResultsAdapter: RecyclerAdapterResults
    private lateinit var currentTab: TextView

    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        supportActionBar?.hide()

        currentTab = popularMenuButton

        viewModel = MovieListViewModel.getInstance(application)

        setUpTabs()
        setUpPager()
        setUpSearchPanel()
        setUpResultPanel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setUpPager() {
        popularPage = Page(viewModel.popularMovies.value!!, searchContainer, resources, this)
        topPage = Page(viewModel.topMovies.value!!, searchContainer, resources, this)

        vpAdapter = PagerAdapter(arrayListOf(popularPage, topPage))
        vpMovies.adapter = vpAdapter

        subscribeLists()

        vpMovies.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                try {
                    if (vpMovies.currentItem == 0) viewModel.pullPopular()
                    else viewModel.pullTop()

                    changeTabsSize(vpMovies.currentItem)
                } catch (e: UninitializedPropertyAccessException) {
                    Log.e("ListActivity", "onPageSelected: UninitializedPropertyAccessException ")
                }
            }
        })
    }

    private fun subscribeLists() {
        viewModel.popularMovies.observe(this) {
            try {
                vpAdapter.pages[0].movies = viewModel.popularMovies.value ?: ArrayList()
            } catch (e: Exception) {
                Log.e("Activity", "subscribeLists: subscribe popular lambda", e)
            }
        }
        viewModel.topMovies.observe(this) {
            try {
                vpAdapter.pages[1].movies = viewModel.topMovies.value ?: ArrayList()
            } catch (e: Exception) {
                Log.e("Activity", "subscribeLists: subscribe top lambda", e)
            }
        }
    }

    private fun changeTabsSize(currentItem: Int) {
        when (currentItem) {
            0 -> execAnimation(popularMenuButton)
            1 -> execAnimation(topMenuButton)
        }
    }

    private fun execAnimation(newSelected: TextView) {
        AnimatorInflater.loadAnimator(this, R.animator.tab_animation_decrease)
            .apply {
                setTarget(currentTab)
                start()
            }
        AnimatorInflater.loadAnimator(this, R.animator.tab_animation_grow)
            .apply {
                setTarget(newSelected)
                start()
            }
        currentTab = newSelected
    }

    private fun setUpTabs() {
        popularMenuButton.setOnClickListener { vpMovies.currentItem = 0 }
        topMenuButton.setOnClickListener { vpMovies.currentItem = 1 }
    }

    private fun setUpSearchPanel() {
        ibBackSearch.visibility = View.GONE
        ibBackSearch.setOnClickListener { searchBackClicked() }
        ibFind.setOnClickListener { findClicked() }

        edSearch.setOnFocusChangeListener { _, b -> editSearchFocusChanged(b) }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edSearch.text.toString() == "") {
                    ibFind.visibility = View.GONE
                } else {
                    ibFind.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setUpResultPanel() {
        rvResultsAdapter = RecyclerAdapterResults(viewModel.searchResult.value ?: ArrayList())
        rvResults.adapter = rvResultsAdapter
        viewModel.searchResult.observe(this) {
            try {
                rvResultsAdapter.results = viewModel.searchResult.value ?: ArrayList()
            } catch (e: Exception) {
                Log.e("Activity", "subscribeLists: subscribe results lambda", e)
            }
        }
    }

    private fun editSearchFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            edSearch.setHintTextColor(resources.getColor(R.color.colorPrimaryDark))
            ibBackSearch.visibility = View.VISIBLE
            ibFind.visibility = View.GONE

            vpMovies.visibility = View.GONE
            tabsContainer.visibility = View.GONE
        } else {
            edSearch.setHintTextColor(resources.getColor(R.color.colorWhite))
            ibBackSearch.visibility = View.GONE
            ibFind.visibility = View.VISIBLE
            vpMovies.visibility = View.VISIBLE
            tabsContainer.visibility = View.VISIBLE
        }
    }

    private fun searchBackClicked() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(ibBackSearch.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        edSearch.text = "".toEditable()
        edSearch.clearFocus()
        viewModel.clearResult()

        rvResults.visibility = View.GONE
        ibBackSearch.visibility = View.GONE

        ibFind.visibility = View.VISIBLE
        tabsContainer.visibility = View.VISIBLE
        vpMovies.visibility = View.VISIBLE
    }

    private fun findClicked() {
        viewModel.search(edSearch.text.toString())

        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(ibBackSearch.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        rvResults.visibility = View.VISIBLE
    }

    private fun String.toEditable(): Editable? = Editable.Factory.getInstance().newEditable(this)
}