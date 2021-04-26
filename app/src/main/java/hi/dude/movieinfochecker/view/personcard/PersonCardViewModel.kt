package hi.dude.movieinfochecker.view.personcard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.model.entities.Person
import hi.dude.movieinfochecker.model.entities.PersonMovie
import hi.dude.movieinfochecker.model.repository.Repository
import hi.dude.movieinfochecker.view.moviecard.MovieCardActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PersonCardViewModel @Inject constructor(): ViewModel() {

    companion object {
        const val TAG = "PersonCardViewModel"
    }

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var adapter: MovieRecyclerAdapter

    @Inject
    lateinit var handler: CoroutineExceptionHandler
    lateinit var personInfo: LiveData<Person>

    fun bindGeneral(recycler: RecyclerView, activity: Activity) {
        recycler.adapter = adapter
        adapter.onMovieClicked = {
            val intent = Intent(activity, MovieCardActivity::class.java)
            intent.putExtra("id", it)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    fun loadPersonInfo(id: String) {
        personInfo = repository.personInfo
        viewModelScope.launch {
            repository.loadPersonInfo(id)
        }
    }

    fun updateData(text: PersonCardActivity.TextViews, photo: ImageView) {
        updatePhoto(photo)
        updateText(text)
        adapter.movies = personInfo.value?.knownFor ?: ArrayList()
        loadImages()
    }

    private fun updateText(views: PersonCardActivity.TextViews) {
        views.name.text = personInfo.value?.name
        views.role.text = personInfo.value?.role
        views.summary.text = personInfo.value?.summary
        try {
            views.birthDate.text = LocalDate.parse(personInfo.value?.birthDate)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
        } catch (e: NullPointerException) {
            Log.e(TAG, "updateText: NPE")
        } catch (e: ParseException) {
            Log.e(TAG, "updateText: ParseException")
        }
    }

    private fun updatePhoto(photo: ImageView) = try {
            Picasso.get()
                .load(personInfo.value?.imageUrl)
                .placeholder(R.color.colorBlack)
                .error(R.color.colorBlack)
                .resize(photo.width, photo.height)
                .centerCrop()
                .into(photo)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "updatePhoto: IllegalArgumentException ${e.message}")
        }

    private fun loadImages() {
        adapter.movies.forEachIndexed { position, movie ->
            val loadJob = viewModelScope.launch(handler) {
                withContext(Dispatchers.IO) {
                    movie.bitmap = loadImage(movie)
                }
            }
            viewModelScope.launch(handler) {
                loadJob.join()
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun loadImage(movie: PersonMovie): Bitmap =
        Picasso.get()
            .load(movie.imageUrl)
            .resizeDimen(R.dimen.actor_photo_width, R.dimen.actor_photo_height)
            .centerCrop()
            .get()

}