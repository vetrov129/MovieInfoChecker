package hi.dude.movieinfochecker.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import hi.dude.movieinfochecker.model.entities.MovieShort

@Database(entities = [MovieShort::class], version = 1)
abstract class DaoGetter: RoomDatabase() {
    abstract fun getFavorDao(): FavorDao
}