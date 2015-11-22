package com.n8.intouch.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.client.DataSnapshot

import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebase: Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InTouchApplication.graph.inject(this)

        setContentView(R.layout.activity_main)

        firebase.child("goo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                var data: String = snapshot?.getValue(String::class.java) ?: "error"
                Toast.makeText(getApplication(), data, Toast.LENGTH_LONG).show();
            }

            override fun onCancelled(p0: FirebaseError?) {

            }

        })
    }
}
