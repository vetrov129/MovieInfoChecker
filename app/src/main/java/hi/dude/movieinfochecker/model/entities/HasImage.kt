package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap

interface HasImage {
    val imageUrl: String?
    var bitmap: Bitmap?
}