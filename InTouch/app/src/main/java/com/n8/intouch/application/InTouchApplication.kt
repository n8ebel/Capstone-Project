package com.n8.intouch.application

import android.app.Activity
import android.app.Application
import android.preference.PreferenceManager
import android.widget.Toast
import com.firebase.client.Firebase
import com.n8.intouch.common.CurrentActivityProvider

class InTouchApplication : Application(), CurrentActivityProvider {

    companion object {
        lateinit var component: ApplicationComponent
    }

    lateinit var activity:Activity

    override fun onCreate() {
        super.onCreate()
        Firebase.setAndroidContext(this);

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this, this))
                .build()
    }

    // region Implements CurrentActivityProvider

    override fun getCurrentActivity(): Activity {
        return activity
    }

    // endregion Implements CurrentActivityProvider
}