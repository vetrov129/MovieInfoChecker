package hi.dude.movieinfochecker.application

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import hi.dude.movieinfochecker.model.network.NetworkModule
import hi.dude.movieinfochecker.model.repository.RepositoryModule
import hi.dude.movieinfochecker.view.moviecard.MovieCardActivity
import hi.dude.movieinfochecker.view.main.fragments.favorites.FavoritesFragment
import hi.dude.movieinfochecker.view.main.fragments.movielist.MovieListFragment
import hi.dude.movieinfochecker.view.main.fragments.search.SearchFragment
import hi.dude.movieinfochecker.view.personcard.PersonCardActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(fragment: MovieListFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SearchFragment)
    fun inject(activity: MovieCardActivity)
    fun inject(activity: PersonCardActivity)
}