package hi.dude.movieinfochecker

import android.animation.AnimatorInflater
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import hi.dude.movieinfochecker.models.Movie
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.coroutines.*

class MovieListActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var vpAdapter: PagerAdapter
    private lateinit var currentTab: TextView

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        supportActionBar?.hide()

        App.imageWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128f, resources.displayMetrics).toInt()
        App.imageHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 176f, resources.displayMetrics).toInt()
        App.resources = resources

        currentTab = popularMenuButton

        setUpTabs()
        setUpPager()
        setUpSearchPanel()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun setUpPager() {
        val popular = Page(ArrayList(), searchContainer)
        val top = Page(ArrayList(), searchContainer)

        vpAdapter = PagerAdapter(arrayListOf(popular, top))
        vpMovies.adapter = vpAdapter

        vpMovies.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                try {
                    var getter: suspend () -> ArrayList<Movie> = { App.connector.getMostPopularMovies() }
                    if (vpMovies.currentItem == 1) getter = { App.connector.getTop250Movies() }

                    launch {
                        vpAdapter.pages[vpMovies.currentItem].pullDataIfNeed(getter)
                    }
                    changeTabsSize(vpMovies.currentItem)
                } catch (e: UninitializedPropertyAccessException) {
                    Log.e("ListActivity", "onPageSelected: UninitializedPropertyAccessException ")
                }

            }
        })

        pullMovieList()
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

    private fun pullMovieList() = launch {
        val movies = withContext(Dispatchers.IO) { App.connector.getMostPopularMovies() }
        vpAdapter.pages[vpMovies.currentItem].apply {
            this.movies = movies
            pullImages()
        }
    }

    private fun setUpSearchPanel() {
        ibBackSearch.visibility = View.GONE
        ibBackSearch.setOnClickListener { searchBackClicked() }

        edSearch.setOnFocusChangeListener { _, b -> editSearchFocusChanged(b) }

//        edSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (edSearch.text.toString() == "") {
//                    ibClearSearch.visibility = View.GONE
//                    resultContainer.visibility = View.GONE
//                    hintsContainer.visibility = View.VISIBLE
//                } else {
//                    runSearch(edSearch.text.toString(), 4)
//
//                    hintsContainer.visibility = View.GONE
//                    ibClearSearch.visibility = View.VISIBLE
//                    resultContainer.visibility = View.VISIBLE
//                    tvShowMore.visibility = View.VISIBLE
//                }
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {}
//        })
    }

    private fun editSearchFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            edSearch.setHintTextColor(resources.getColor(R.color.colorPrimaryDark))
            ibBackSearch.visibility = View.VISIBLE
            ibSearch.visibility = View.GONE

            vpMovies.visibility = View.GONE
            tabsContainer.visibility = View.GONE
        } else {
            edSearch.setHintTextColor(resources.getColor(R.color.colorWhite))
            ibBackSearch.visibility = View.GONE
            ibSearch.visibility = View.VISIBLE
            vpMovies.visibility = View.VISIBLE
            tabsContainer.visibility = View.VISIBLE
        }
    }

    private fun searchBackClicked() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(ibBackSearch.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        edSearch.text = "".toEditable()
        edSearch.clearFocus()

        vpMovies.visibility = View.VISIBLE
        ibBackSearch.visibility = View.GONE
        ibSearch.visibility = View.VISIBLE
        tabsContainer.visibility = View.VISIBLE
    }

    private fun String.toEditable(): Editable? = Editable.Factory.getInstance().newEditable(this)
}