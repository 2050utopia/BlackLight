package info.papdt.blacklight

import android.app.Application
import com.karumi.dexter.Dexter

/**
 * Created by peter on 7/2/16.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Dexter.initialize(this)
    }
}
