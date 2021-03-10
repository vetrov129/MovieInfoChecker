package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

class SimilarMovie(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("image") val image: String,
    @SerializedName("plot") val plot: String,
    @SerializedName("directors") val directors: String,
    @SerializedName("stars") val stars: String,
    @SerializedName("genres") val genres: String,
    @SerializedName("imDbRating") val imDbRating: String,
)