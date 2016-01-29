package com.n8.intouch.signin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.common.BackPressedListener

/**
 *  Root-level sign-in screen that will handle coordination of other
 *  screens ie: add account, enter credentials, other auth modes.
 */
class SignInFragment : Fragment(), CredentialEntryFragment.Listener, BackPressedListener {

    val TAG_CREDENTIAL_FRAGMENT = "credential_fragment"

    val TAG_ADD_ACCOUNT_FRAGMENT = "add_account_fragment"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_sign_in, container, false)

        val credentialFragment = CredentialEntryFragment()
        credentialFragment.listener = this

        childFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, credentialFragment, TAG_CREDENTIAL_FRAGMENT)
                .addToBackStack(TAG_CREDENTIAL_FRAGMENT)
                .commit()

        return rootView
    }

    override fun addAccountClicked() {
        val fragment = AddAccountFragment()

        childFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, TAG_ADD_ACCOUNT_FRAGMENT)
                .addToBackStack(TAG_ADD_ACCOUNT_FRAGMENT)
                .commit()
    }

    // region Implements BackPressedListener

    override fun onBackPressed(): Boolean {
        val addFragment = childFragmentManager.findFragmentByTag(TAG_ADD_ACCOUNT_FRAGMENT)
        if (addFragment is BackPressedListener && !addFragment.onBackPressed()) {
            childFragmentManager.popBackStack()
            return true
        }

        val credFragment = childFragmentManager.findFragmentByTag(TAG_CREDENTIAL_FRAGMENT)
        if (credFragment is BackPressedListener && !credFragment.onBackPressed()) {
            childFragmentManager.popBackStack()
            return true
        }

        return false
    }

    // endregion Implements BackPressedListener

}