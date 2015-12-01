package com.n8.intouch.addeventscreen

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by n8 on 11/30/15.
 */
@Module
class AddEventModule(var view:AddEventView) {

    @Provides
    fun providesInteractor(context:Context) : AddEventInteractor{
        return AddEventInteractorImpl(context)
    }

    @Provides
    fun providesPresenter(interactor: AddEventInteractor) : AddEventPresenter{
        return AddEventPresenterImpl(view, interactor)
    }
}