package com.n8.intouch

import android.app.Application
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

    /**
     * Allow the application context to be injected but require that it be annotated with
     * [ ][ForApplication] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideFirebase(): Firebase {
        return Firebase(application.getString(R.string.firebase_url))
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(application.javaClass.simpleName, Context.MODE_PRIVATE)
    }
}
