package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

class ResultItem(
    @SerializedName("id") val id: String?,
    @SerializedName("resultType") val resultType: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
) {
    var imageBitmap: Bitmap? = null

//    suspend fun pullImage() {
//        try {
//            val connection = URL(imageUrl).openConnection()
//            connection.doInput = true
//            connection.connect()
//            imageBitmap = BitmapFactory.decodeStream(connection.getInputStream()).scale(App.imageWidth, App.imageHeight)
//
//        } catch (e: FileNotFoundException) {
//            Log.e("Movie", "pullImage: url $imageUrl")
//        }
//    }
}