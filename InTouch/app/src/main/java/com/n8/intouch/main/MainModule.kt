package com.n8.intouch.main

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    fun providesGoobar(): String{
        return "Goobar"
    }

    @Provides
    fun providesFoo(): Foo{
        return Foo()
    }
}