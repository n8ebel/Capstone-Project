package com.n8.intouch.application

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.firebase.client.Firebase
import com.n8.intouch.R
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.data.FirebaseEventsDataManager
import com.n8.intouch.model.FirebaseUser
import com.n8.intouch.model.User
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application, private val activityProvider:CurrentActivityProvider) {

    @Provides
    fun provideApplicationContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideFirebase(): Firebase {
        return Firebase(application.getString(R.string.firebase_url))
    }

    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(application.javaClass.simpleName, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideContentResolver(): ContentResolver {
        return application.contentResolver
    }

    @Provides
    fun provideCurrentActivityProvider(): CurrentActivityProvider {
        return activityProvider
    }

    @Provides
    fun provideCurrentUser(firebase: Firebase) : User {
        return FirebaseUser(firebase.auth)
    }

    @Provides
    fun provideEventsDataManager(firebase: Firebase) : EventsDataManager {
        return FirebaseEventsDataManager(firebase)
    }
}
