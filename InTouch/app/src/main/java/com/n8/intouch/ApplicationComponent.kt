package com.n8.intouch

import android.content.Context
import android.content.SharedPreferences
import com.firebase.client.Firebase
import com.n8.intouch.InTouchApplication
import com.n8.intouch.main.MainActivity
import com.n8.intouch.main.MainComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun getFirebase() : Firebase

    fun getSharedPreferences() : SharedPreferences

    fun getContext() : Context
}
