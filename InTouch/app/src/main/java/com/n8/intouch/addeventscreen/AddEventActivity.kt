package com.n8.intouch.addeventscreen

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Event
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.n8.intouch.InTouchApplication

import com.n8.intouch.R
import javax.inject.Inject

/**
 * Activity to host different fragments that allow the user to create/edit events
 */
class AddEventActivity : AppCompatActivity() {

    val BACKSTACK_TAG = "add_fragment"

    companion object Factory {

        val ARG_CONTACT_URI = "contactUri"

        fun createAddForDateIntent(context:Context, contactUri: Uri): Intent {
            var intent = Intent(context, AddEventActivity::class.java)
            intent.putExtra(ARG_CONTACT_URI, contactUri)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        if (savedInstanceState != null) {
            return
        }

        var contactUri = intent?.getParcelableExtra<Uri>(ARG_CONTACT_URI)
        if (contactUri != null) {
            showFragment(contactUri)
        } else {
            throw IllegalStateException("Activity was not launched with valid arguments.  AddActivity.Factory should be used to create intents")
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    private fun showFragment(contactUri: Uri) {
        var fragment = AddEventFragment()
        var component = DaggerAddEventComponent.builder().
                applicationComponent(InTouchApplication.graph).
                addEventModule(AddEventModule(contactUri, fragment)).
                build()
        fragment.component = component

        supportFragmentManager.beginTransaction().
                add(R.id.fragmentContainer, fragment).
                addToBackStack(BACKSTACK_TAG).
                commit()
    }
}
