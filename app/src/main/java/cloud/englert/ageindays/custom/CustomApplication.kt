package cloud.englert.ageindays.custom

import android.app.Application

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeSetup.applyTheme(this)
    }
}