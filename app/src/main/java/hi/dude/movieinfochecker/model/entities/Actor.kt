package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id") val id: String?,
    @SerializedName("image") override val imageUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("asCharacter") val asCharacter: String?,
): HasImage {
    override var bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}