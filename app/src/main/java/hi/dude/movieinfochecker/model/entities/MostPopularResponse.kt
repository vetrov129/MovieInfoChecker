package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class MostPopularResponse(
    @SerializedName("items") val items: ArrayList<MovieShort>
)