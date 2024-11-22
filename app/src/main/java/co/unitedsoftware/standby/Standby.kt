package co.unitedsoftware.standby

import android.app.Application
import com.google.android.material.color.DynamicColors

class Standby : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}