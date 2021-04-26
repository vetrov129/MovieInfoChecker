package hi.dude.movieinfochecker.model.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import hi.dude.movieinfochecker.model.room.BitmapConverter

@Entity(tableName = "movies")
@TypeConverters(BitmapConverter::class)
open class MovieShort(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("title") val title: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("image") override val imageUrl: String?,
    @SerializedName("imDbRating") val rating: String?,
): HasImage {
    override var bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}
