package com.n8.intouch.application

import android.app.AlarmManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.firebase.client.Firebase
import com.n8.intouch.alarm.EventScheduler
import com.n8.intouch.browsescreen.di.BrowseComponent
import com.n8.intouch.common.BaseComponent
import com.n8.intouch.common.CurrentActivityProvider
import com.n8.intouch.data.EventsDataManager
import com.n8.intouch.model.User
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent : BaseComponent {
    fun getFirebase() : Firebase

    fun getSharedPreferences() : SharedPreferences

    fun getContext() : Context

    fun getContentResolver() : ContentResolver

    fun getCurrentActivityProvider() : CurrentActivityProvider

    fun getCurrentUser() : User

    fun getDataManager() : EventsDataManager

    fun getEventScheduler() : EventScheduler

    fun getAlarmManager() : AlarmManager
}
