package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class ListOfResult(
    @SerializedName("results") val results: ArrayList<ResultItem>?
)