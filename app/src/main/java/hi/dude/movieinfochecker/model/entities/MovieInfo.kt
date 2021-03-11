package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

class MovieInfo(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String?,
    @SerializedName("originalTitle") val originalTitle: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("runtimeMins") val runtimeMin: String?,
    @SerializedName("runtimeStr") val runtimeStr: String?,
    @SerializedName("plot") val plot: String?,
    @SerializedName("directorList") val directorList: List<Person>?,
    @SerializedName("writerList") val writerList: List<Person>?,
    @SerializedName("starList") val starList: List<Person>?,
    @SerializedName("stars") val stars: String?,
    @SerializedName("writers") val writers: String?,
    @SerializedName("directors") val directors: String?,
    @SerializedName("actorList") val actorList: List<Actor>?,
    @SerializedName("genres") val genres: String?,
    @SerializedName("companies") val companies: String?,
    @SerializedName("countries") val countries: String?,
    @SerializedName("contentRating") val contentRating: String?,
    @SerializedName("imDbRating") val imDbRating: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("similars") val similars: List<SimilarMovie>?,
) {

    // empty fields
    constructor() : this(
        "", "", "", "", "", "", "", "", "", "",
        ArrayList(), ArrayList(), ArrayList(), "", "", "", ArrayList(), "", "",
        "", "", "", "", ArrayList()
    )
}