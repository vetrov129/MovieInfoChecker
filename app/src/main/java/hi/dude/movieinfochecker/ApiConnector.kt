package hi.dude.movieinfochecker

import android.util.Log
import com.google.gson.Gson
import hi.dude.movieinfochecker.models.ListOfMovie
import hi.dude.movieinfochecker.models.Movie
import java.net.URL

class ApiConnector {

    private companion object {
        const val API_KEY = "/k_5ueqi95l"
        const val API_URL = "https://imdb-api.com/en/API/"

        enum class REQUEST(val text: String) {
            MOST_POPULAR_MOVIES("MostPopularMovies"),
            TOP_250_MOVIES("Top250Movies"),

        }
    }

    private val gson = Gson()

    private fun getJson(request: REQUEST, vararg params: Pair<String, String>): String {
        var url = "$API_URL${request.text}$API_KEY"
        for (param in params) {
            url += "/" + param.first + "=" + param.second
        }
        Log.i("ApiConnector", "getJson: $url")
        return URL(url).readText()
    }

    fun getMostPopularMovies(): ArrayList<Movie> {
        val json = getJson(REQUEST.MOST_POPULAR_MOVIES)
        return gson.fromJson(json, ListOfMovie::class.java).items ?: ArrayList()
    }
}