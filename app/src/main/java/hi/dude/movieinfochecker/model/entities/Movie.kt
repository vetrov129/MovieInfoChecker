package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class Movie(
    @SerializedName("id") val id: String?,
    @SerializedName("rank") val rank: String?,
    @SerializedName("rankUpDown") val growth: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("crew") val crew: String?,
    @SerializedName("imDbRating") val rating: String?,
    @SerializedName("imDbRatingCount") val ratingCount: String?,
) {
    var imageBitmap: Bitmap? = null
}