package com.n8.intouch.browsescreen


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.application.InTouchApplication

import com.n8.intouch.R
import com.n8.intouch.common.TitleProvider
import com.n8.intouch.getComponent

/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment(), TitleProvider {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_contacts, container, false)
    }

    // region Implements TitleProvider

    override fun getTitle(): String {
        // Use app context here so if this fragment is nested in another, and its title is needed before
        // it's created, the string resource can be found
        //
        return InTouchApplication.component.getContext().getString(R.string.contacts)
    }

    // endregion Implements TitleProvider
}
