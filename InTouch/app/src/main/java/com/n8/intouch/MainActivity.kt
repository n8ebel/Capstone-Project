package com.n8.intouch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.firebase.client.Firebase
import com.n8.intouch.dependency_injection.DaggerApplicationComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebase: Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InTouchApplication.graph.inject(this)

        setContentView(R.layout.activity_main)

        firebase.child("foo").setValue("goobaroo")
    }
}
