package com.n8.intouch.main

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Singleton
    @Provides
    fun providesGoobar(): String{
        return "Goobar"
    }
}