package hi.dude.movieinfochecker.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import androidx.core.graphics.scale
import com.google.gson.annotations.SerializedName
import hi.dude.movieinfochecker.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.net.URL

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

    suspend fun pullImage() {
        try {
            val connection = URL(imageUrl).openConnection()
            connection.doInput = true
            connection.connect()
            imageBitmap = BitmapFactory.decodeStream(connection.getInputStream()).scale(App.imageWidth, App.imageHeight)

        } catch (e: FileNotFoundException) {
            Log.e("Movie", "pullImage: url $imageUrl")
        }
    }
}