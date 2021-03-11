package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.IOException

interface WithPoster {

    var imageBitmap: Bitmap?
    val imageUrl: String?

    suspend fun pullPoster(logMessage: Any? = null) {
        if (imageUrl == null) return
        val result = CoroutineScope(Dispatchers.IO).async {
            Log.i("Movie", "pullPoster $logMessage: start $imageUrl")
            try {
                Picasso.get()
                    .load(imageUrl)
                    .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
                    .centerCrop()
                    .error(R.drawable.empty)
                    .placeholder(R.drawable.empty)
                    .get()
            } catch (e: IOException) {
                Log.e("Movie", "pullPoster: $logMessage", e)
            }
        }
        imageBitmap = result.await() as Bitmap
        Log.i("Movie", "pullPoster $logMessage: end $imageUrl")
    }
}