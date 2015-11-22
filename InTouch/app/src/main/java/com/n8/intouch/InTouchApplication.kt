package com.n8.intouch

import android.app.Application
import android.widget.Toast
import com.firebase.client.Firebase
import com.n8.intouch.DaggerApplicationComponent

class InTouchApplication : android.app.Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit public var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        Firebase.setAndroidContext(this);

        graph = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        graph.inject(this)
    }
}