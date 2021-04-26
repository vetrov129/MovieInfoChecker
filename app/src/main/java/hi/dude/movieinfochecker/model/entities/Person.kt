package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("image") override val imageUrl: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("awards") val awards: String?,
    @SerializedName("knownFor") val knownFor: ArrayList<PersonMovie>?,
): HasImage {
    override var bitmap: Bitmap? = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}
