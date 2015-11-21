package com.n8.intouch

import android.app.Application
import com.firebase.client.Firebase

class InTouchApplication : android.app.Application() {
    val firebase: Firebase by lazy {
        Firebase(getString(R.string.firebase_url))
    }

    override fun onCreate() {
        super.onCreate()
        Firebase.setAndroidContext(this);
    }
}