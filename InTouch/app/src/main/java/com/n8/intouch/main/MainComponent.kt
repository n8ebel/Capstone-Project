package com.n8.intouch.main

import com.n8.intouch.ApplicationComponent
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface MainComponent {
    fun inject(foo: Foo)

    fun inject(mainActivity: MainActivity)
}