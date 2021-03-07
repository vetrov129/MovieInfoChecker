package hi.dude.movieinfochecker

import android.app.Application

class App : Application() {

    companion object{
        lateinit var connector: ApiConnector
            private set
    }

    override fun onCreate() {
        super.onCreate()
        connector = ApiConnector()
    }
}