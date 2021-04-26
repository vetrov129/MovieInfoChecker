package hi.dude.movieinfochecker.application

import android.app.Application

class App: Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().context(this).build()
    }
}