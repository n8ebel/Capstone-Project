package com.n8.intouch.main

import android.content.ContentResolver
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.n8.intouch.contentprovider.ProviderContract

class MainInteractorImpl(val firebase: Firebase) : MainInteractor {
    override fun handleClick(body: (result:String) -> Unit) {

        firebase.child("goo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                var data: String = snapshot?.getValue(String::class.java) ?: "error"

                return body(data)
            }

            override fun onCancelled(p0: FirebaseError?) {
                return body("there was an error")
            }

        })
    }

    override fun getData(contentResolver: ContentResolver, arg1: String, arg2: Array<String>) {
        contentResolver.query(ProviderContract.BASE_CONTENT_URI, null, arg1, arg2, null)
    }
}