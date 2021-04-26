package hi.dude.movieinfochecker.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.Lazy
import hi.dude.movieinfochecker.model.entities.*
import hi.dude.movieinfochecker.model.network.ImdbApi
import hi.dude.movieinfochecker.model.room.FavorDao
import hi.dude.movieinfochecker.view.main.fragments.search.SearchType
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Singleton
class Repository @Inject constructor(private var api: Lazy<ImdbApi>) {

    companion object {
        const val TAG = "Repository"
    }

    @Inject
    lateinit var handler: CoroutineExceptionHandler

    @Inject
    lateinit var moviesList: MutableLiveData<ArrayList<MovieShort>>

    @Inject
    lateinit var movieInfoStack: StackLiveData<MovieLong>

    @Inject
    lateinit var favorDao: FavorDao

    @Inject
    lateinit var favorSet: HashSet<String>

    @Inject
    lateinit var favorList: MutableLiveData<ArrayList<MovieShort>>

    @Inject
    lateinit var results: MutableLiveData<SearchResponse>

    @Inject
    lateinit var hints: MutableLiveData<ArrayList<ResultItem>>

    @Inject
    lateinit var personInfo: MutableLiveData<Person>

    suspend fun pullMoviesList() = withContext(Dispatchers.IO) {
        fillFavor() // TODO: 22.04.2021 вызов этого метода здесь нелогичен, нужно вынести

        api.get().getPopular().enqueue(object : Callback<MostPopularResponse> {
            override fun onResponse(call: Call<MostPopularResponse>, response: Response<MostPopularResponse>) {
                Log.i(TAG, "pullMoviesList onResponse: size = ${response.body()?.items?.size}")
                CoroutineScope(Dispatchers.Main).launch(handler) { moviesList.value = response.body()?.items }
            }

            override fun onFailure(call: Call<MostPopularResponse>, t: Throwable) {
                Log.e(TAG, "pullMoviesList onFailure: request ${call.request().url()}", t)
            }
        })
    }

    private suspend fun fillFavor() = withContext(Dispatchers.IO) {
        favorList.value?.clear()
        favorList.value?.addAll(favorDao.getFavorMovies())
        Log.i(TAG, "fillFavor: favors size = ${favorList.value?.size}")
        favorList.value?.forEach { favorSet.add(it.id) }
    }

    suspend fun addFavor(favor: MovieShort) = withContext(Dispatchers.IO) {
        favorList.value?.add(favor)
        favorSet.add(favor.id)
        favorDao.addToFavor(favor)
    }

    suspend fun removeFavor(favor: MovieShort) = withContext(Dispatchers.IO) {
        favorList.value?.remove(favor)
        favorSet.remove(favor.id)
        favorDao.removeFromFavor(favor)
    }

    suspend fun pullMovieInfo(id: String) = withContext(Dispatchers.IO) {
        api.get().getMovieInfo(id).enqueue(object : Callback<MovieLong> {
            override fun onResponse(call: Call<MovieLong>, response: Response<MovieLong>) {
                Log.i(TAG, "pullMovieInfo onResponse: title = ${response.body()?.title}")
                CoroutineScope(Dispatchers.Main).launch(handler) { movieInfoStack.push(response.body()) }
            }

            override fun onFailure(call: Call<MovieLong>, t: Throwable) {
                Log.e(TAG, "pullMovieInfo onFailure: request ${call.request().url()}", t)
            }
        })
    }

    suspend fun fillSearchResult(query: String, type: SearchType) = withContext(Dispatchers.IO) {
        val callback = object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                Log.i(TAG, "fillSearchResult onResponse: size = ${response.body()?.results?.size}")
                removeUnnecessaryResults(response.body()?.results)
                CoroutineScope(Dispatchers.Main).launch(handler) { results.value = response.body() }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "fillSearchResult onFailure: request ${call.request().url()}", t)
            }
        }
        val call = api.get().run {
            when (type) {
                SearchType.MOVIE -> searchMovie(query)
                SearchType.SERIES -> searchSeries(query)
                SearchType.NAME -> searchName(query)
                SearchType.ALL -> searchAll(query)
            }
        }
        call.enqueue(callback)
    }

    private fun removeUnnecessaryResults(list: ArrayList<ResultItem>?) {
        list?.removeIf { it.type == "Company" || it.type == "Keyword" }
    }

    suspend fun pullHints(query: String) = withContext(Dispatchers.IO) {
        api.get().getHints(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                Log.i(TAG, "pullHints onResponse: size = ${response.body()?.results?.size}")
                CoroutineScope(Dispatchers.Main).launch(handler) { hints.value = response.body()?.results }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "pullHints onFailure: request ${call.request().url()}", t)
            }
        })
    }

    suspend fun loadPersonInfo(id: String) = withContext(Dispatchers.IO) {
        api.get().getPersonInfo(id).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                Log.i(TAG, "loadPersonInfo onResponse: size = ${response.body()?.name}")
                CoroutineScope(Dispatchers.Main).launch(handler) { personInfo.value = response.body() }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                Log.e(TAG, "loadPersonInfo onFailure: request ${call.request().url()}", t)
            }
        })
    }
}