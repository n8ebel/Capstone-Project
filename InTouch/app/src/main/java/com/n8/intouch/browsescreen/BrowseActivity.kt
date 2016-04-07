package com.n8.intouch.browsescreen

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
import com.n8.intouch.getComponent
import javax.inject.Inject

class BrowseActivity : BaseActivity() {

    companion object {
        val TAG_BROWSE_FRAGMENT = "browse"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Only add the tabbed fragment the first time the activity is created
        //
        if (savedInstanceState == null) {
            var browseFragment = BrowseFragment()
            browseFragment.component = DaggerBrowseComponent.builder().
                    applicationComponent(application.getComponent()).
                    browseModule(BrowseModule(browseFragment)).
                    build()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, browseFragment)
                    .addToBackStack(TAG_BROWSE_FRAGMENT)
                    .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }
}
