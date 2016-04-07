package com.n8.intouch.browsescreen.di

import com.firebase.client.Firebase
import com.n8.intouch.application.ApplicationModule
import com.n8.intouch.browsescreen.BrowseContract
import com.n8.intouch.browsescreen.BrowsePresenter
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.model.FirebaseUser
import com.n8.intouch.model.User

import dagger.Module
import dagger.Provides

@Module
class BrowseModule(val viewController: BrowseContract.ViewController) {

    @Provides
    fun provideBrowsePresenter(currentActivityProvider: CurrentActivityProvider) : BrowsePresenter {
        return BrowsePresenter(currentActivityProvider, viewController)
    }

    @Provides
    fun provideCurrentUser(firebase: Firebase) : User {
        return FirebaseUser(firebase.auth)
    }
}