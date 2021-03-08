package hi.dude.movieinfochecker.models

import com.google.gson.annotations.SerializedName

data class ListOfResult(
    @SerializedName("results") val results: ArrayList<ResultItem>?
)