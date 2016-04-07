package com.n8.intouch.browsescreen

import android.content.Intent
import android.os.Bundle
import com.n8.intouch.R
import com.n8.intouch.browsescreen.di.BrowseModule
import com.n8.intouch.browsescreen.di.DaggerBrowseComponent
import com.n8.intouch.common.BaseActivity
import com.n8.intouch.getComponent

class BrowseActivity : BaseActivity() {

    companion object {
        val TAG_BROWSE_FRAGMENT = "BrowseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Only add the tabbed fragment the first time the activity is created
        //
        if (savedInstanceState == null) {
            var browseFragment = BrowseFragment().apply {
                component = DaggerBrowseComponent.builder().
                        applicationComponent(application.getComponent()).
                        browseModule(BrowseModule(this)).
                        build()
            }

            with(supportFragmentManager.beginTransaction()){
                add(R.id.fragmentContainer, browseFragment, TAG_BROWSE_FRAGMENT)
                addToBackStack(TAG_BROWSE_FRAGMENT)
                commit()
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        supportFragmentManager.findFragmentByTag(TAG_BROWSE_FRAGMENT)?.onActivityResult(requestCode, resultCode, data)
    }
}
