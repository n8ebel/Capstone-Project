package com.n8.intouch.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.client.DataSnapshot

import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.n8.intouch.InTouchApplication
import com.n8.intouch.R
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity(), MainView {

    val TAG = "MainActivity"

    // region Implements MainView

    override fun showProgress() {
        Log.d(TAG, "showing progress")
    }

    override fun hideProgress() {
        Log.d(TAG, "hide progress")
    }

    override fun showResult(result: String) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }

    // endregion Implements MainView

    @Inject
    lateinit var firebase: Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InTouchApplication.graph.inject(this)

        setContentView(R.layout.activity_main)

        var presenter = MainPresenterImpl(this, MainInteractorImpl(firebase))

        var button = findViewById(R.id.theButton)
        button.setOnClickListener {
            presenter.onClick()
        }
    }
}
