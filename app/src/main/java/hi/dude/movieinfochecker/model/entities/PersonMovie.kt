package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class PersonMovie(
    @SerializedName("id") val id: String,
    @SerializedName("role") val role: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("image") override val imageUrl: String?,
): HasImage {
    override var bitmap: Bitmap? = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}
