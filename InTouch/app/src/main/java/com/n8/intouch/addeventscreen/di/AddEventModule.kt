package com.n8.intouch.addeventscreen.di

import android.content.Context
import android.net.Uri
import com.firebase.client.Firebase
import com.n8.intouch.addeventscreen.AddEventContract
import com.n8.intouch.addeventscreen.AddEventPresenter
import com.n8.intouch.addeventscreen.data.ContactLoader
import com.n8.intouch.addeventscreen.data.ContentProviderContactLoader
import com.n8.intouch.alarm.EventScheduler
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.data.FirebaseEventsDataManager
import com.n8.intouch.model.User
import dagger.Module
import dagger.Provides

@Module
class AddEventModule(val contactUri: Uri, val viewController: AddEventContract.ViewController) {

    @Provides
    fun provideContactUri() : Uri {
        return contactUri
    }

    @Provides
    fun providInteractor(context: Context) : ContactLoader {
        return ContentProviderContactLoader(context, contactUri)
    }

    @Provides
    fun providePresenter(
            currentActivityProvider: CurrentActivityProvider,
            interactor: ContactLoader,
            user: User,
            dataManager:EventsDataManager,
            eventScheduler: EventScheduler) : AddEventContract.UserInteractionListener {

        return AddEventPresenter(currentActivityProvider, viewController, interactor, user, dataManager, eventScheduler)
    }
}