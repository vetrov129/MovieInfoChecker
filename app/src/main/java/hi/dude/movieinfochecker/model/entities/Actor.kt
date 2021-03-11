package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

class Actor(
    @SerializedName("id") val id: String,
    @SerializedName("image") override val imageUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("asCharacter") val asCharacter: String?,
): WithPoster {
    override var imageBitmap: Bitmap? = null
}
