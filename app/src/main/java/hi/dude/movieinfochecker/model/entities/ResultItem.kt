package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class ResultItem(
    @SerializedName("id") val id: String,
    @SerializedName("resultType") val type: String?,
    @SerializedName("image") override val imageUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
): HasImage {
    override var bitmap: Bitmap? = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}
