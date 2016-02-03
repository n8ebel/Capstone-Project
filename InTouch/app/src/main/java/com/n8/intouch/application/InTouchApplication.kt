package com.n8.intouch.application

import android.app.Application
import android.widget.Toast
import com.firebase.client.Firebase

class InTouchApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit public var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        Firebase.setAndroidContext(this);

        graph = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }
}