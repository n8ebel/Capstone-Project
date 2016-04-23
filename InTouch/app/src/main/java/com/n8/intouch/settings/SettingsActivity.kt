package com.n8.intouch.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.n8.intouch.R
import com.n8.intouch.common.BaseActivity
import com.n8.intouch.setupBackNavigation

class SettingsActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context) : Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        (findViewById(R.id.toolbar) as Toolbar).apply {
            setSupportActionBar(this)
            setupBackNavigation {
                onBackPressed()
            }
        }

        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).commit();
    }

}