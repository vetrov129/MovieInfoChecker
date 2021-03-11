package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

class Trailer(
    @SerializedName("imDbId") val imDbId: String,
    @SerializedName("title") val title: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("videoId") val videoId: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
) {
    constructor(): this("", "", "", "", "", "")
}