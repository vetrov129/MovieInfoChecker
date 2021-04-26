package hi.dude.movieinfochecker.model.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import dagger.Module
import dagger.Provides
import hi.dude.movieinfochecker.model.entities.*
import hi.dude.movieinfochecker.model.room.DaoGetter
import hi.dude.movieinfochecker.model.room.FavorDao
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun getMoviesList(): MutableLiveData<ArrayList<MovieShort>> {
        val data = MutableLiveData<ArrayList<MovieShort>>()
        data.value = ArrayList()
        return data
    }

    @Provides
    fun getCoroutineHandler() = CoroutineExceptionHandler { context, throwable ->
        Log.e("Coroutine", "$context ", throwable)
    }

    @Provides
    fun getMovieStack() = StackLiveData<MovieLong>()

    @Singleton
    @Provides
    fun getDaoGetter(context: Context): DaoGetter {
        return  Room.databaseBuilder(context, DaoGetter::class.java, "movies.sqlite")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun getFavorDao(daoGetter: DaoGetter): FavorDao = daoGetter.getFavorDao()

    @Provides
    fun getFavorSet() = HashSet<String>()

    @Provides
    fun getResults() = MutableLiveData<SearchResponse>()

    @Provides
    fun getHints(): MutableLiveData<ArrayList<ResultItem>> {
        val data = MutableLiveData<ArrayList<ResultItem>>()
        data.value = ArrayList()
        return data
    }

    @Provides
    fun getPersonInfo() = MutableLiveData<Person>()

}