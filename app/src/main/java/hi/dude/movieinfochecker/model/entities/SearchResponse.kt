package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("searchType") val type: String?,
    @SerializedName("expression") val expression: String?,
    @SerializedName("results") val results: ArrayList<ResultItem>?,
)