package com.n8.intouch.main

import com.firebase.client.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(val mainView: MainView) {

    @Provides
    fun providesGoobar(): String {
        return "Goobar"
    }

    @Provides
    fun providesFoo(): Foo {
        return Foo()
    }

    @Provides
    fun providesInteractor(firebase: Firebase): MainInteractor {
        return MainInteractorImpl(firebase)
    }

    @Provides
    fun providesPresenter(interactor: MainInteractor): MainPresenter {
        return MainPresenterImpl(mainView, interactor)
    }

}