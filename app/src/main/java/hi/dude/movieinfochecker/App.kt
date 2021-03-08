package hi.dude.movieinfochecker

import android.app.Application
import android.content.res.Resources

class App : Application() {

    companion object{
        lateinit var connector: ApiConnector
            private set

        var imageWidth = 0
        var imageHeight = 0
        lateinit var resources: Resources
    }

    override fun onCreate() {
        super.onCreate()
        connector = ApiConnector()
    }
}