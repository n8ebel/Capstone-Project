package com.n8.intouch.common

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
            return
        }

        val fragment = getCurrentFragment()
        if (fragment!= null && fragment is BackPressedListener && fragment.onBackPressed()) {
            return
        }

        super.onBackPressed()
    }

    private fun getCurrentFragment() : Fragment? {
        var fragmentTag: String? = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)?.name ?: return null

        return supportFragmentManager.findFragmentByTag(fragmentTag)

    }
}