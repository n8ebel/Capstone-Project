package com.n8.intouch.main

import com.n8.intouch.ApplicationComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun inject(foo: Foo)
}