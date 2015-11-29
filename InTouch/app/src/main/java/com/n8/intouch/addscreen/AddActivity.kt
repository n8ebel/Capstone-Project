package com.n8.intouch.addscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast

import com.n8.intouch.R

/**
 * Activity to host different fragments that allow the user to create/edit events
 */
class AddActivity : AppCompatActivity() {

    val BACKSTACK_TAG = "add_fragment"

    companion object Factory {
        val ARG_TYPE = "type"

        private val TYPE_ADD_FOR_DATE = "add_for_date"

        private val TYPE_ADD_CUSTOM = "add_custom"

        fun createAddForDateIntent(context:Context): Intent {
            var intent = Intent(context, AddActivity::class.java)
            intent.putExtra(ARG_TYPE, TYPE_ADD_FOR_DATE)
            return intent
        }

        fun createAddForCustomeIntent(context:Context): Intent {
            var intent = Intent(context, AddActivity::class.java)
            intent.putExtra(ARG_TYPE, TYPE_ADD_CUSTOM)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        if (intent?.getStringExtra(ARG_TYPE) == TYPE_ADD_FOR_DATE) {
            showAddForDate(savedInstanceState)
        }else if (intent?.getStringExtra(ARG_TYPE) == TYPE_ADD_CUSTOM) {
            showAddForCustom(savedInstanceState)
        } else {
            throw IllegalStateException("Activity was not launched with valid arguments.  AddActivity.Factory should be used to create intents")
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    private fun showAddForDate(savedInstanceState:Bundle?) {
        if (savedInstanceState != null) {
            return
        }

        supportFragmentManager.beginTransaction().
                add(R.id.fragmentContainer, AddForDateFragment()).
                addToBackStack(BACKSTACK_TAG).
                commit()
    }

    private fun showAddForCustom(savedInstanceState:Bundle?){
        if (savedInstanceState != null) {
            return
        }

        supportFragmentManager.beginTransaction().
                add(R.id.fragmentContainer, AddForCustomFragment()).
                addToBackStack(BACKSTACK_TAG).
                commit()
    }

}
