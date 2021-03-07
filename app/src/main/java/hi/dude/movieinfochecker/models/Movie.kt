package hi.dude.movieinfochecker.models

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

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
    val imageBitmap: Bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
}