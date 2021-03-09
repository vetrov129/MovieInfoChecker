package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ResultItem(
    @SerializedName("id") val id: String?,
    @SerializedName("resultType") val resultType: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
) {
    var imageBitmap: Bitmap? = null
}