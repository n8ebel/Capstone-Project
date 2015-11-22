package com.n8.intouch

import com.firebase.client.Firebase
import com.n8.intouch.InTouchApplication
import com.n8.intouch.main.MainActivity
import com.n8.intouch.main.MainComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: InTouchApplication)

    fun inject(mainActivity: MainActivity)

    fun provideFirebase() : Firebase
}
