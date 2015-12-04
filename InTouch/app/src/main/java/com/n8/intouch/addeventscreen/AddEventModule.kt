package com.n8.intouch.addeventscreen

import android.content.Context
import android.net.Uri
import dagger.Module
import dagger.Provides

/**
 * Created by n8 on 11/30/15.
 */
@Module
class AddEventModule(val contactUri:Uri, val view:AddEventView) {

    @Provides
    fun provideContactUri() : Uri {
        return contactUri
    }

    @Provides
    fun providInteractor(context:Context) : AddEventInteractor {
        return AddEventInteractorImpl(context, contactUri)
    }

    @Provides
    fun providePresenter(interactor: AddEventInteractor) : AddEventPresenter {
        return AddEventPresenterImpl(view, interactor)
    }
}