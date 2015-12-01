package com.n8.intouch.addeventscreen

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by n8 on 11/30/15.
 */
@Module
class AddModule(val view:AddEventView) {

    @Provides
    fun providInteractor(context:Context) : AddEventInteractor {
        return AddEventInteractorImpl(context)
    }

    @Provides
    fun providePresenter(interactor: AddEventInteractor) : AddEventPresenter {
        return AddEventPresenterImpl(view)
    }
}