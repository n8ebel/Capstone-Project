package com.n8.intouch.signin

import android.os.AsyncTask
import com.firebase.client.AuthData
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError

open class SignInAsyncTask(private val firebase:Firebase, private val authHandler: Firebase.AuthResultHandler) : AsyncTask<String, Void, Void>() {

    override fun doInBackground(vararg params: String?): Void? {
        firebase.authWithPassword(params[0], params[1], authHandler)

        return null
    }

}