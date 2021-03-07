package hi.dude.movieinfochecker.models

import com.google.gson.annotations.SerializedName

data class ListOfMovie(
    @SerializedName("items") val items: ArrayList<Movie>?
)