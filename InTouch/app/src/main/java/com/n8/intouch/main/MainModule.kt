package com.n8.intouch.main

import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun providesGoobar(): String{
        return "Goobar"
    }
}