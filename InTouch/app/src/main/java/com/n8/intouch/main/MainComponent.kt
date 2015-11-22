package com.n8.intouch.main

import dagger.Component

@Component(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun inject(foo: Foo)

//    interface MainComponent {
//        fun inject(mainActivity: MainActivity)
//    }

}