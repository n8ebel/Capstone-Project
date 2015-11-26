package com.n8.intouch.main

import com.n8.intouch.ActivityScope
import com.n8.intouch.ApplicationComponent
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}