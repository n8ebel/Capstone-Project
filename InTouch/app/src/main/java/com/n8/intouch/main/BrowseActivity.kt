package com.n8.intouch.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.widget.Toast

import com.n8.intouch.application.InTouchApplication
import com.n8.intouch.R
import com.n8.intouch.browsescreen.BrowseFragment
import com.n8.intouch.browsescreen.di.BrowseModule
import com.n8.intouch.browsescreen.di.DaggerBrowseComponent
import com.n8.intouch.common.BackPressedListener
import com.n8.intouch.common.BaseActivity
import javax.inject.Inject

class BrowseActivity : BaseActivity() {
    val TAG = "MainActivity"

    val FRAG_BROWSE = "browse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Only add the tabbed fragment the first time the activity is created
        //
        if (savedInstanceState == null) {
            var browseFragment = BrowseFragment()
            browseFragment.component = DaggerBrowseComponent.builder().
                    applicationComponent(InTouchApplication.graph).
                    browseModule(BrowseModule(browseFragment, browseFragment)).
                    build()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, browseFragment)
                    .addToBackStack(FRAG_BROWSE)
                    .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }
}
