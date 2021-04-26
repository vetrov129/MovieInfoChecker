package hi.dude.movieinfochecker.model.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import hi.dude.movieinfochecker.model.entities.MovieShort

@Dao
interface FavorDao {

    @Query("SELECT * FROM movies;")
    fun getFavorMovies(): List<MovieShort>

    @Insert
    fun addToFavor(movie: MovieShort)

    @Delete
    fun removeFromFavor(movie: MovieShort)
}