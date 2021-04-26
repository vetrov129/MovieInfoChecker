package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class ComingSoon(
    @SerializedName("items") val items: List<MovieShort>?
)