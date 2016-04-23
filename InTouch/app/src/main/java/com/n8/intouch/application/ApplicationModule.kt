package com.n8.intouch.application

import android.app.AlarmManager
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.firebase.client.Firebase
import com.n8.intouch.R
import com.n8.intouch.alarm.AlarmManagerEventScheduler
import com.n8.intouch.alarm.EventScheduler
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
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    fun provideContentResolver(): ContentResolver {
        return application.contentResolver
    }

    @Provides
    fun provideAlarmManager() : AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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

    @Provides
    fun provideEventsScheduler(context: Context, sharedPreferences: SharedPreferences, alarmManager: AlarmManager) : EventScheduler {
        return AlarmManagerEventScheduler(context, sharedPreferences, alarmManager)
    }
}
