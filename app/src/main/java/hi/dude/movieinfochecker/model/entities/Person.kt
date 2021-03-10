package hi.dude.movieinfochecker.model.entities

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
)