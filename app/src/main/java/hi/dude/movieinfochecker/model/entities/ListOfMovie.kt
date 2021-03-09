package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class ListOfMovie(
    @SerializedName("items") val items: ArrayList<Movie>?
)