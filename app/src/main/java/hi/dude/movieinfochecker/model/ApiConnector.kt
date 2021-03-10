package hi.dude.movieinfochecker.model

import android.util.Log
import com.google.gson.Gson
import hi.dude.movieinfochecker.model.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.UnknownHostException

class ApiConnector {

    private companion object {
        const val API_URL = "https://imdb-api.com/en/API/"

        var keyIndex = 0
        val keyArray = listOf(
            "k_5ueqi95l",
            "k_cr7y5r14",
            "k_gc21cr51",
            "k_uxavvxrw",
            "k_bzwwaycy",
            "k_ud4t73ua",
            "k_5pt6eck2",
            "k_2z1nnwrz",
            "k_sq8s4kq3",
            "k_t0cev46r",
            "k_9h03b220",
            "k_u8sgda2p",
            "k_mks835pj",
            "k_1w1a06so",
            "k_7oyp1gka"
        )
        var API_KEY = "/${keyArray[keyIndex]}"

        const val NEED_UPDATE_KEY_RESPONSE = "of 100 per day)\"}"

        enum class REQUEST(val text: String) {
            MOST_POPULAR_MOVIES("MostPopularMovies"),
            TOP_250_MOVIES("Top250Movies"),
            SEARCH("Search"),
            INFO("Title")
        }
    }

    private val gson = Gson()

    private fun updateKey() {
        try {
            keyIndex++
            API_KEY = "/${keyArray[keyIndex]}"
            Log.i("ApiConnector", "updateKey: key ${keyIndex + 1}/${keyArray.size}")
        } catch (e: IndexOutOfBoundsException) {
            Log.e("ApiConnector", "updateKey: the keys are out, daily request limit exceeded")
        }
    }

    private suspend fun getJson(request: REQUEST, vararg params: String): String {
        while (true) {
            var url = "$API_URL${request.text}$API_KEY"
            for (param in params) {
                url += "/$param"
            }
            var json = ""
            try {
                withContext(Dispatchers.IO) { json = URL(url).readText() }
            } catch (e: UnknownHostException) {
                Log.e("ApiConnector", "getJson: error ${e.message} $url")
                return ""
            }
            if (json.endsWith(NEED_UPDATE_KEY_RESPONSE))   // key change if the limit is exceeded
                updateKey()
            else {
                Log.i("ApiConnector", "getJson: $url")
                return json
            }
        }
    }

    suspend fun getMostPopularMovies(): ArrayList<Movie> {
        val json = getJson(REQUEST.MOST_POPULAR_MOVIES)
        return gson.fromJson(json, ListOfMovie::class.java).items ?: ArrayList()
    }

    suspend fun getTop250Movies(): ArrayList<Movie> {
        val json = getJson(REQUEST.TOP_250_MOVIES)
        return gson.fromJson(json, ListOfMovie::class.java).items ?: ArrayList()
    }

    suspend fun search(query: String): ArrayList<ResultItem> {
        val json = getJson(REQUEST.SEARCH, query)
        return gson.fromJson(json, ListOfResult::class.java).results ?: ArrayList()
    }

    suspend fun getInfoById(id: String): MovieInfo {
        val json = getJson(REQUEST.INFO, id)
        return gson.fromJson(json, MovieInfo::class.java)
    }
}