package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class MovieLong(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("releaseDate") val date: String?,
    @SerializedName("runtimeStr") val duration: String?,
    @SerializedName("plot") val plot: String?,
    @SerializedName("actorList") val actorList: ArrayList<Actor>?,
    @SerializedName("genres") val genres: String?,
    @SerializedName("countries") val countries: String?,
    @SerializedName("similars") val similars: ArrayList<MovieShort>?,
    @SerializedName("year") val year: String?,
    @SerializedName("imDbRating") val rating: String?,
)