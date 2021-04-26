package hi.dude.movieinfochecker.model.network

import hi.dude.movieinfochecker.model.entities.MostPopularResponse
import hi.dude.movieinfochecker.model.entities.MovieLong
import hi.dude.movieinfochecker.model.entities.Person
import hi.dude.movieinfochecker.model.entities.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApi {

    companion object {
        const val key = "k_5ueqi95l"
//        const val key = "k_nbsf0frb"
//        const val key = "k_m6mlx7up"
    }

    @GET("MostPopularMovies/$key")
    fun getPopular(): Call<MostPopularResponse>

    @GET("Title/$key/{id}")
    fun getMovieInfo(@Path("id") id: String): Call<MovieLong>

    @GET("SearchMovie/$key/{query}")
    fun searchMovie(@Path("query") query: String): Call<SearchResponse>

    @GET("SearchSeries/$key/{query}")
    fun searchSeries(@Path("query") query: String): Call<SearchResponse>

    @GET("SearchName/$key/{query}")
    fun searchName(@Path("query") query: String): Call<SearchResponse>

    @GET("SearchAll/$key/{query}")
    fun searchAll(@Path("query") query: String): Call<SearchResponse>

    @GET("SearchKeyword/$key/{query}")
    fun getHints(@Path("query") query: String): Call<SearchResponse>

    @GET("Name/$key/{id}")
    fun getPersonInfo(@Path("id") id: String): Call<Person>
}