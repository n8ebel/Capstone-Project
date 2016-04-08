package com.n8.intouch.common

import android.os.Bundle
import android.support.v4.app.Fragment

open class BaseFragment : Fragment(), BackPressedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    // region Implements BackPressedListener

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }

        return false
    }

    // endregion Implements BackPressedListener

}