package com.n8.intouch.main

import android.content.Context
import com.firebase.client.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(val mainView: MainView) {

    @Provides
    fun providesInteractor(context: Context, firebase: Firebase): MainInteractor {
        return MainInteractorImpl(context, firebase)
    }

    @Provides
    fun providesPresenter(interactor: MainInteractor): MainPresenter {
        return MainPresenterImpl(mainView, interactor)
    }

}