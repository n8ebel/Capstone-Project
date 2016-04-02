package com.n8.intouch.application

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.firebase.client.Firebase
import com.n8.intouch.R
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun provideApplicationContext(): Context {
        return application
    }

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
}
