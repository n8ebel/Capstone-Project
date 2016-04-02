package com.n8.intouch.application

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.firebase.client.Firebase
import com.n8.intouch.common.BaseComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent : BaseComponent {
    fun getFirebase() : Firebase

    fun getSharedPreferences() : SharedPreferences

    fun getContext() : Context

    fun getContentResolver() : ContentResolver
}
