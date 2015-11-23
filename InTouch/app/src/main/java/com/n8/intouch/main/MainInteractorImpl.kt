package com.n8.intouch.main

import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener

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

}