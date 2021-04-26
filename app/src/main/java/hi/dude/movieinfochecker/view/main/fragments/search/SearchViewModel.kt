package hi.dude.movieinfochecker.view.main.fragments.search

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.ResultItem
import hi.dude.movieinfochecker.model.entities.SearchResponse
import hi.dude.movieinfochecker.model.repository.Repository
import hi.dude.movieinfochecker.view.moviecard.MovieCardActivity
import hi.dude.movieinfochecker.view.personcard.PersonCardActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
    }

    @Inject
    lateinit var repository: Repository
    lateinit var hints: LiveData<ArrayList<ResultItem>>
    lateinit var results: LiveData<SearchResponse>

    @Inject
    lateinit var handler: CoroutineExceptionHandler

    @Inject
    lateinit var adapterResults: ResultRecyclerAdapter

    @Inject
    lateinit var adapterHints: HintsRecyclerAdapter
    lateinit var radioGroupMap: HashMap<Int, SearchType>

    var currentType = SearchType.MOVIE

    private fun fillRadioGroupMap() {
        radioGroupMap = HashMap()
        radioGroupMap[R.id.rbMovie] = SearchType.MOVIE
        radioGroupMap[R.id.rbSeries] = SearchType.SERIES
        radioGroupMap[R.id.rbPerson] = SearchType.NAME
        radioGroupMap[R.id.rbAll] = SearchType.ALL
    }

    fun bindGeneral(results: RecyclerView, hints: RecyclerView, radioGroup: RadioGroup, context: Context) {
        this.hints = repository.hints
        this.results = repository.results

        fillRadioGroupMap()
        radioGroup.setOnCheckedChangeListener { rg, _ -> onRadioButtonCheckedChanged(rg) }

        hints.adapter = adapterHints
        results.adapter = adapterResults
        (results.layoutManager as StaggeredGridLayoutManager).spanCount = 2

        adapterResults.onItemClicked = { onResultItemClicked(it, context) }
    }

    private fun onResultItemClicked(id: String, context: Context) {
        val clazz =
            if (id.startsWith("nm")) PersonCardActivity::class.java
            else MovieCardActivity::class.java
        val intent = Intent(context, clazz)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

    fun bindSearch(field: EditText, button: ImageButton, manager: InputMethodManager?) {
        field.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModelScope.launch(handler) {
//                    repository.pullHints(text.toString()) //todo закомментировано для экономии запросов
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {}
        })
        button.setOnClickListener { onSearchClicked(field.text.toString(), button.windowToken, manager) }
        adapterHints.onHintClicked = {
            field.setText(it.replace('-', ' '))
            field.setSelection(it.length)
        }
    }

    private fun onRadioButtonCheckedChanged(rg: RadioGroup) {
        currentType = radioGroupMap[rg.checkedRadioButtonId] ?: SearchType.MOVIE
    }

    private fun onSearchClicked(query: String, windowToken: IBinder, manager: InputMethodManager?) {
        manager?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        viewModelScope.launch(handler) { repository.fillSearchResult(query, currentType) }
    }

    fun onResultsChanged() {
        adapterHints.hints = ArrayList()
        adapterResults.results = results.value?.results ?: ArrayList()
        loadImages()
    }

    fun onHintsChanged(field: EditText) {
        adapterResults.results = ArrayList()
        adapterHints.hints =
            if (field.text.isEmpty()) ArrayList()
            else hints.value ?: ArrayList()
    }

    private fun loadImages() {
        if (results.value?.results == null) return
        results.value?.results?.forEachIndexed { position, item ->
            val loadJob = viewModelScope.launch(handler) {
                loadImage(item)
            }
            viewModelScope.launch(handler) {
                loadJob.join()
                adapterResults.notifyItemChanged(position)
            }
        }
    }

    private suspend fun loadImage(item: ResultItem) = withContext(Dispatchers.IO) {
        item.bitmap = Picasso.get()
            .load(item.imageUrl)
            .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
//            .onlyScaleDown()
            .centerCrop()
            .get()
    }
}