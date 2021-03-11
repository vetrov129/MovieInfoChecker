package hi.dude.movieinfochecker.model.entities

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

interface WithPoster {

    var imageBitmap: Bitmap?
    val imageUrl: String?

//    suspend fun pullPoster(logMessage: Any? = null) {
//        if (imageUrl == null) return
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                Log.i("Movie", "pullPoster $logMessage: start $imageUrl")
//                imageBitmap = Picasso.get()
//                    .load(imageUrl)
//                    .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
//                    .error(R.drawable.empty)
//                    .placeholder(R.drawable.empty)
//                    .get()
//                Log.i("Movie", "pullPoster $logMessage: end $imageUrl")
//            } catch (e: IOException) {
//                Log.e("Movie", "pullPoster: $logMessage", e)
//            }
//        }
//    }

    suspend fun pullPoster(logMessage: Any? = null) {
        if (imageUrl == null) return
        val result = CoroutineScope(Dispatchers.IO).async {
            Log.i("Movie", "pullPoster $logMessage: start $imageUrl")
            try {
                Picasso.get()
                    .load(imageUrl)
                    .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
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