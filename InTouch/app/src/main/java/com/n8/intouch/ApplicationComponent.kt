package com.n8.intouch

import com.n8.intouch.InTouchApplication
import com.n8.intouch.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: InTouchApplication)

    fun inject(mainActivity: MainActivity)
}
