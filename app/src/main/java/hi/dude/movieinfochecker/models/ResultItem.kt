package hi.dude.movieinfochecker.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import com.google.gson.annotations.SerializedName
import hi.dude.movieinfochecker.App
import java.io.FileNotFoundException
import java.net.URL

class ResultItem(
    @SerializedName("id") val id: String?,
    @SerializedName("resultType") val resultType: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
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