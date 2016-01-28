package com.n8.intouch.addeventscreen.di

import android.content.Context
import android.net.Uri
import com.n8.intouch.addeventscreen.AddEventContract
import com.n8.intouch.addeventscreen.AddEventPresenter
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.data.ContentProviderContactLoader
import dagger.Module
import dagger.Provides

@Module
class AddEventModule(val contactUri: Uri, val view: AddEventContract.View) {

    @Provides
    fun provideContactUri() : Uri {
        return contactUri
    }

    @Provides
    fun providInteractor(context: Context) : ContactLoader {
        return ContentProviderContactLoader(context, contactUri)
    }

    @Provides
    fun providePresenter(interactor: ContactLoader) : AddEventContract.UserInteractionListener {
        return AddEventPresenter(view, interactor)
    }
}