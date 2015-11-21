package com.n8.intouch

import android.app.Application
import android.widget.Toast
import com.firebase.client.Firebase
import com.n8.intouch.dependency_injection.ApplicationComponent
import com.n8.intouch.dependency_injection.ApplicationModule
import com.n8.intouch.dependency_injection.DaggerApplicationComponent
import javax.inject.Inject
import javax.inject.Named

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